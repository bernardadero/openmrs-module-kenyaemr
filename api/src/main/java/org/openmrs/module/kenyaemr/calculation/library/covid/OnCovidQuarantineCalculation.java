/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.kenyaemr.calculation.library.covid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Program;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.module.kenyacore.calculation.AbstractPatientCalculation;
import org.openmrs.module.kenyacore.calculation.BooleanResult;
import org.openmrs.module.kenyacore.calculation.Filters;
import org.openmrs.module.kenyaemr.metadata.COVIDMetadata;
import org.openmrs.module.metadatadeploy.MetadataUtils;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Calculates whether a patient is currently enrolled in Covid program
 */
public class OnCovidQuarantineCalculation extends AbstractPatientCalculation{
	
	protected static final Log log = LogFactory.getLog(OnCovidQuarantineCalculation.class);
	
	@Override
	public CalculationResultMap evaluate(Collection<Integer> cohort, Map<String, Object> parameterValues,
                                         PatientCalculationContext context) {
		
		Program covidProgram = MetadataUtils.existing(Program.class, COVIDMetadata._Program.COVID_QUARANTINE);
		Set<Integer> alive = Filters.alive(cohort, context);
		Set<Integer> inCovidProgram = Filters.inProgram(covidProgram, alive, context);
		
		CalculationResultMap ret = new CalculationResultMap();
		for (Integer ptId : cohort) {
			
			boolean onCovid = false;
			if (inCovidProgram.contains(ptId)) {
				onCovid = true;
			}
			
			ret.put(ptId, new BooleanResult(onCovid, this));
		}
		return ret;
	}

}