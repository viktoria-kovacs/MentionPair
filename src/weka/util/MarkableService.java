package weka.util;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Range;

import weka.domain.Markable;

import static weka.Container.config;

public class MarkableService {

	public <T extends Markable> List<T> filterLeafs(List<T> markables) {
		List<T> leafMarkables = new ArrayList<>(markables);

		List<T> markedToRemove = new ArrayList<>();
		do {
			markedToRemove = new ArrayList<>();

			Iterator<T> leafMarkableIterator = leafMarkables.iterator();
			while (leafMarkableIterator.hasNext()) {
				T leafMarkable = leafMarkableIterator.next();
				for (T otherMarkable : leafMarkables) {
					if (!leafMarkable.equals(otherMarkable)
							&& otherMarkable.getSpan().encloses(leafMarkable.getSpan())) {
						markedToRemove.add(leafMarkable);
					}
				}
			}

			for (T markable : markedToRemove) {
				leafMarkables.remove(markable);
			}
		} while (!markedToRemove.isEmpty());

		return sortAndUnique(leafMarkables);
	}

	public <T extends Markable> List<T> sortAndUnique(List<T> markables) {
		Set<Range<Integer>> addedSpans = new HashSet<>(markables.size());
		markables.removeIf(m -> !addedSpans.add(m.getSpan())); // Set.add returns true if this set did not already contain the specified element
				
		Collections.sort(markables, (markable1, markable2) -> {
			return ComparisonChain.start()
					.compare(markable1.getSpan().lowerEndpoint(), markable2.getSpan().lowerEndpoint())
					.compare(markable1.getSpan().upperEndpoint(), markable2.getSpan().upperEndpoint())
					.result();
		});
		return markables;
	}

//	public long getCpsBetween(List<Markable> markables, Range<Integer> headSpan, Range<Integer> footSpan) {
//		Range<Integer> ends = Range.closed(headSpan.upperEndpoint(), footSpan.upperEndpoint());
//		return markables.stream()
//				.filter(markable -> ends.contains(markable.getSpan().upperEndpoint()) || ends.contains(markable.getSpan().lowerEndpoint()))
//				.count();
//	}

	public long getCpsBetween2(List<Markable> markables, Range<Integer> headSpan, Range<Integer> footSpan, String pronType) {
		Range<Integer> ends = Range.closed(headSpan.lowerEndpoint(), footSpan.upperEndpoint());
		
		List<Range<Integer>> relevantCpRanges = markables.stream()
				.map(Markable::getSpan)
				.filter(range -> range.isConnected(ends))
	//			.filter(range -> ends.contains(range.lowerEndpoint()) || ends.contains(range.upperEndpoint())) 
				.collect(Collectors.toList());

		long threePoints = filterRoots(relevantCpRanges).stream()
				.flatMap(range -> Stream.of(range.lowerEndpoint(), range.upperEndpoint()))
				.distinct()
				.filter(ends::contains)
				.count() / 2;

		List<Integer> lowerEndpoints = relevantCpRanges.stream()
                .map(Range::lowerEndpoint)
                .filter(endpoint -> !endpoint.equals(headSpan.lowerEndpoint()))
                .distinct()
                .collect(Collectors.toList());
        int countLower = 0;
        for (int lowerEndpoint : lowerEndpoints) {
            if (!lowerEndpoints.contains(lowerEndpoint + 1)) {
                if (ends.contains(lowerEndpoint)) {
                    countLower++;
                }
            }
        }
        List<Integer> upperEndpoints = relevantCpRanges.stream()
                .map(Range::upperEndpoint)
                .filter(endpoint -> !endpoint.equals(footSpan.upperEndpoint()))
                .distinct()
                .collect(Collectors.toList());
        int countUpper = 0;
        for (int upperEndpoint : upperEndpoints) {
            if (!upperEndpoints.contains(upperEndpoint + 1)) {
                if (ends.contains(upperEndpoint)) {
                    countUpper++;
                }
            }
        }
        return countLower + countUpper + threePoints;
	}

	private List<Range<Integer>> filterRoots(List<Range<Integer>> ranges) {
		return ranges.stream()
				.filter(range -> ranges.stream()
						.filter(r -> !r.equals(range))
						.noneMatch(otherRange -> otherRange.encloses(range)))
				.collect(Collectors.toList());
	}
	
	public long getCpsBetween(List<Markable> markables, Range<Integer> headSpan, Range<Integer> footSpan) {
		Range<Integer> ends = Range.closedOpen(headSpan.upperEndpoint(), footSpan.lowerEndpoint());

		List<Range<Integer>> relevantCpRanges = markables.stream()
				.map(Markable::getSpan)
				.filter(range -> range.isConnected(ends))
	//			.filter(range -> ends.contains(range.lowerEndpoint()) || ends.contains(range.upperEndpoint())) 
				.collect(Collectors.toList()); 
		
		List<Integer> upperEndpoints = relevantCpRanges.stream()
	                .map(Range::upperEndpoint)
	                .distinct()
	                .collect(Collectors.toList());
	        int countUpper = 0;
	        for (int upperEndpoint : upperEndpoints) {
	            if (!upperEndpoints.contains(upperEndpoint + 1)) {
	                if (ends.contains(upperEndpoint)) {
	                    countUpper++;
	                }
	            }
	        }
		return countUpper;
	}

	

	public long getNpsBetween(List<Markable> markables, Range<Integer> headSpan, Range<Integer> footSpan) {
		Range<Integer> ends = Range.openClosed(headSpan.upperEndpoint(), footSpan.lowerEndpoint());
		return markables.stream()
				.filter(markable -> ends.contains(markable.getSpan().upperEndpoint()))
				.count();
		
		
//		boolean same = markables.stream()
//				.anyMatch(markable -> {
//					Range<Integer> span = markable.getSpan();
//					return span.encloses(headSpan) && span.encloses(footSpan);
//				});
//
//		if (same) return 0;
//		
//		Range<Integer> gap = headSpan.gap(footSpan);
//		boolean next = markables.stream()
//				.noneMatch(markable -> gap.encloses(markable.getSpan()));
//		
//		if (next) return 1;
//
//		Range<Integer> fullSpan = headSpan.gap(footSpan); // span from head to foot
//
//		long npsBetween = markables.stream()
//				.filter(markable -> fullSpan.encloses(markable.getSpan()))
//				.count();
//
//		return npsBetween + 1;
	}

//	public long getCpsBetween(List<Markable> cps, Range<Integer> headSpan, Range<Integer> footSpan) {
//		Integer headCpId = null;
//		Integer footCpId = null;
//
//		for (Markable cp : cps) {
//			if (cp.getSpan().encloses(headSpan)) {
//				headCpId = cp.getId();
//			} else if (headCpId == null && cp.getSpan().lowerEndpoint() > headSpan.upperEndpoint()) {
//				// fix for missing leaf cps
//				headCpId = cp.getId() - 1;
//			}
//
//			if (cp.getSpan().encloses(footSpan)) {
//				footCpId = cp.getId();
//			} else if (footCpId == null && cp.getSpan().lowerEndpoint() > footSpan.upperEndpoint()) {
//				// fix for missing leaf cps
//				footCpId = cp.getId();
//			}
//		}
//
//		if (headCpId == null || footCpId == null) {
//			System.out.println("Something's wrong with headCpId or footCpId");
//		}
//
//		return Math.abs(headCpId - footCpId);
//	}

}
