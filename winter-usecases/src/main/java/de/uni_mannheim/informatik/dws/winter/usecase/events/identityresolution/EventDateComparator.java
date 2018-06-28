package de.uni_mannheim.informatik.dws.winter.usecase.events.identityresolution;

import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;

import java.util.HashMap;
import java.util.Map;

import de.uni_mannheim.informatik.dws.winter.matching.rules.Comparator;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.similarity.date.YearSimilarity;
import de.uni_mannheim.informatik.dws.winter.usecase.events.utils.BestListSimilarity;
import de.uni_mannheim.informatik.dws.winter.usecase.events.model.Event;

/**
 * {@link Comparator} for {@link Event}s based on the {@link Event#getDates()}
 * values. With a maximal difference of less than a years.
 *
 * @author Daniel Ringler
 *
 */
public class EventDateComparator implements Comparator<Event, Attribute> {
    private static final long serialVersionUID = 1L;
    private BestListSimilarity bestListSimilarity = new BestListSimilarity();
    private YearSimilarity sim = new YearSimilarity(1);
    
    private HashMap<ComparatorDetails, String> comparisonResult = new HashMap<ComparatorDetails, String>();

    @Override
    public double compare(
            Event record1,
            Event record2,
            Correspondence<Attribute, Matchable> schemaCorrespondences) {
    	this.comparisonResult.put(ComparatorDetails.comparatorName, EventDateComparator.class.getName());
    	
    	double similarity = bestListSimilarity.getBestDatesSimilarity(sim, record1.getDates(), record2.getDates());
    	
    	this.comparisonResult.put(ComparatorDetails.record1Value, record1.getDates().toString());
    	this.comparisonResult.put(ComparatorDetails.record2Value, record2.getDates().toString());
    	
    	this.comparisonResult.put(ComparatorDetails.similarity, Double.toString(similarity));
    	
        return similarity;
    }

	@Override
	public Map<ComparatorDetails, String> getComparisonResult() {
		return this.comparisonResult;
	}


}
