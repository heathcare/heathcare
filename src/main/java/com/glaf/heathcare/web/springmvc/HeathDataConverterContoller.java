/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.glaf.heathcare.web.springmvc;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.jxls.common.Context;
import org.jxls.transform.poi.PoiTransformer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.glaf.core.el.ExpressionTools;
import com.glaf.core.util.DateUtils;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.ResponseUtils;

import com.glaf.heathcare.converter.HaizgMedicalExaminationConverter;
import com.glaf.heathcare.converter.HaizgStudentConverter;
import com.glaf.heathcare.domain.MedicalExamination;
import com.glaf.heathcare.domain.Person;
import com.glaf.report.bean.ReportContainer;
import com.glaf.report.data.ReportDefinition;

@Controller("/heathcare/dataConverter")
@RequestMapping("/heathcare/dataConverter")
public class HeathDataConverterContoller {
	protected static final Log logger = LogFactory.getLog(HeathDataConverterContoller.class);

	@RequestMapping("/showUpload")
	public ModelAndView showUpload(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		return new ModelAndView("/heathcare/dataConverter/showUpload", modelMap);
	}

	@RequestMapping(path = "/upload", method = RequestMethod.POST)
	public void upload(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("file") MultipartFile mFile) throws IOException {
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		String rptId = null;
		byte[] data = null;
		ReportDefinition rdf = null;
		InputStream is = null;
		ByteArrayInputStream bais = null;
		ByteArrayOutputStream baos = null;
		BufferedOutputStream bos = null;
		try {
			String type = request.getParameter("type");
			if (StringUtils.equals(type, "person")) {
				HaizgStudentConverter converter = new HaizgStudentConverter();
				List<Person> persons = converter.getPersons(mFile.getInputStream());
				params.put("persons", persons);
				params.put("rows", persons);
				rptId = "youerinfo";
			} else if (StringUtils.equals(type, "exam")) {
				HaizgMedicalExaminationConverter converter = new HaizgMedicalExaminationConverter();
				List<MedicalExamination> exams = converter.getMedicalExaminations(mFile.getInputStream());
				params.put("exams", exams);
				params.put("rows", exams);
				rptId = "mdicaldata";
			}

			if (StringUtils.isNotEmpty(rptId)) {
				rdf = ReportContainer.getContainer().getReportDefinition(rptId);
				if (rdf != null && rdf.getData() != null) {
					bais = new ByteArrayInputStream(rdf.getData());
					is = new BufferedInputStream(bais);
					baos = new ByteArrayOutputStream();
					bos = new BufferedOutputStream(baos);

					Context context2 = PoiTransformer.createInitialContext();

					Set<Entry<String, Object>> entrySet = params.entrySet();
					for (Entry<String, Object> entry : entrySet) {
						String key = entry.getKey();
						Object value = entry.getValue();
						context2.putVar(key, value);
					}

					org.jxls.util.JxlsHelper.getInstance().processTemplate(is, bos, context2);
					IOUtils.closeQuietly(is);
					IOUtils.closeQuietly(bais);

					bos.flush();
					baos.flush();
					data = baos.toByteArray();

					String filename = "export" + DateUtils.getNowYearMonthDayHHmmss() + ".xls";
					if (StringUtils.isNotEmpty(rdf.getExportFilename())) {
						filename = rdf.getExportFilename();
						params.put("yyyyMMdd", DateUtils.getDateTime("yyyyMMdd", new Date()));
						params.put("yyyyMMddHHmm", DateUtils.getDateTime("yyyyMMddHHmm", new Date()));
						params.put("yyyyMMddHHmmss", DateUtils.getDateTime("yyyyMMddHHmmss", new Date()));

						filename = ExpressionTools.evaluate(filename, params);
					}

					ResponseUtils.download(request, response, data, filename);

				}
			}

		} catch (Exception ex) {
			logger.error(ex);
		} finally {
			data = null;
		}
	}
}
