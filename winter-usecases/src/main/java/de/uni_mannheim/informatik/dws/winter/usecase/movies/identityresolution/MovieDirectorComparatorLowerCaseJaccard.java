/*
 * Copyright (c) 2017 Data and Web Science Group, University of Mannheim, Germany (http://dws.informatik.uni-mannheim.de/)
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */
package de.uni_mannheim.informatik.dws.winter.usecase.movies.identityresolution;


import java.util.HashMap;
import java.util.Map;

import de.uni_mannheim.informatik.dws.winter.matching.rules.Comparator;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.similarity.string.TokenizingJaccardSimilarity;
import de.uni_mannheim.informatik.dws.winter.usecase.movies.model.Movie;

/**
 * {@link Comparator} for {@link Movie}s based on the
 * {@link Movie#getDirector()} values, and their
 * {@link TokenizingJaccardSimilarity} similarity, with a lower casing
 * beforehand.
 * 
 * @author Robert Meusel (robert@dwslab.de)
 * @author Oliver Lehmberg (oli@dwslab.de)
 * 
 */
public class MovieDirectorComparatorLowerCaseJaccard implements Comparator<Movie, Attribute> {

	private static final long serialVersionUID = 1L;
	TokenizingJaccardSimilarity sim = new TokenizingJaccardSimilarity();
	
	private HashMap<ComparatorDetails, String> comparisonResult = new HashMap<ComparatorDetails, String>();

	@Override
	public double compare(
			Movie record1,
			Movie record2,
			Correspondence<Attribute, Matchable> schemaCorrespondences) {
		
		this.comparisonResult.put(ComparatorDetails.comparatorName, MovieDirectorComparatorLowerCaseJaccard.class.getName());
		
		// preprocessing
		String s1 = record1.getDirector();
		String s2 = record2.getDirector();
		
		this.comparisonResult.put(ComparatorDetails.record1Value, s1);
		this.comparisonResult.put(ComparatorDetails.record2Value, s2);
		
		if (s1 != null) {
			s1 = s1.toLowerCase();
		} else {
			s1 = "";
		}
		
		if (s2 != null) {
			s2 = s2.toLowerCase();
		} else {
			s2 = "";
		}
		
		this.comparisonResult.put(ComparatorDetails.record1PreprocessedValue, s1);
		this.comparisonResult.put(ComparatorDetails.record2PreprocessedValue, s2);

		// calculate similarity
		double similarity = sim.calculate(s1, s2);
		this.comparisonResult.put(ComparatorDetails.similarity, Double.toString(similarity));

		// postprocessing
		if (similarity <= 0.3) {
			similarity = 0;
		}

		similarity *= similarity;
		
		this.comparisonResult.put(ComparatorDetails.postproccesedSimilarity, Double.toString(similarity));
		
		return similarity;
	}

	@Override
	public Map<ComparatorDetails, String> getComparisonResult() {
		return this.comparisonResult;
	}

}
