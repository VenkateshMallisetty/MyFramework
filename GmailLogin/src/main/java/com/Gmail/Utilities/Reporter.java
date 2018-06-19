package com.Gmail.Utilities;

import com.Gmail.accelerators.TestEngine;
import com.Gmail.Support.ConfiguratorSupport;
import com.Gmail.Support.ReportStampSupport;
public class Reporter extends TestEngine
{
	public static ConfiguratorSupport configProps = new ConfiguratorSupport(
			"config.properties");
	static String timeStamp = ReportStampSupport.timeStamp().replace(":", "_")
			.replace(".", "_");

	public void reportCreater(String browser) throws Throwable {
		int intReporterType = Integer.parseInt(configProps
				.getProperty("reportsType"));

		switch (intReporterType) {
		case 1:

			break;
		case 2:

			htmlCreateReport();
			//HtmlReportSupport.createDetailedReport();

			break;
		default:

			htmlCreateReport();
			break;
		}
	}

}
