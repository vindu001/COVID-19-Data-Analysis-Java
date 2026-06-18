import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        String filePath = "../data/owid-covid-data.csv";

        try {

            BufferedReader br = new BufferedReader(new FileReader(filePath));

            String line;

            br.readLine(); // Skip header

            HashMap<String, String> countryCases = new HashMap<>();

            String latestDate = "";
            String totalCases = "";
            String totalDeaths = "";

            while ((line = br.readLine()) != null) {

                String[] data = line.split(",");

                if (data.length < 8)
                    continue;

                String location = data[2];

                // Skip regions/groups
                if (location.equals("World")
                        || location.equals("Asia")
                        || location.equals("Europe")
                        || location.equals("North America")
                        || location.equals("South America")
                        || location.equals("Africa")
                        || location.equals("Oceania")
                        || location.equals("International")
                        || location.equals("European Union (27)")
                        || location.contains("income")) {
                    continue;
                }

                // Store latest cases
                if (!data[4].trim().isEmpty()) {
                    countryCases.put(location, data[4]);
                }

                // India data
                if (location.equals("India")) {

                    if (!data[4].trim().isEmpty()) {

                        latestDate = data[3];
                        totalCases = data[4];
                        totalDeaths = data[7];
                    }
                }
            }

            br.close();

            // India Analysis
            System.out.println("========== INDIA COVID ANALYSIS ==========");
            System.out.println("Country : India");
            System.out.println("Latest Date : " + latestDate);
            System.out.println("Total Cases : " + totalCases);
            System.out.println("Total Deaths : " + totalDeaths);

            double cases = Double.parseDouble(totalCases);
            double deaths = Double.parseDouble(totalDeaths);

            double recoveryRate =
                    ((cases - deaths) / cases) * 100;

            System.out.printf("Recovery Rate : %.2f%%\n", recoveryRate);

            System.out.println("\nCountries Stored : "
                    + countryCases.size());

            // Sort Countries
            List<Map.Entry<String, String>> list =
                    new ArrayList<>(countryCases.entrySet());

            Collections.sort(list,
                    new Comparator<Map.Entry<String, String>>() {

                        @Override
                        public int compare(
                                Map.Entry<String, String> a,
                                Map.Entry<String, String> b) {

                            double casesA =
                                    Double.parseDouble(a.getValue());

                            double casesB =
                                    Double.parseDouble(b.getValue());

                            return Double.compare(casesB, casesA);
                        }
                    });

            // Top 10 Countries
            System.out.println(
                    "\n========== TOP 10 BY TOTAL CASES ==========");

            int rank = 1;

            for (Map.Entry<String, String> entry : list) {

                System.out.println(
                        rank + ". "
                                + entry.getKey()
                                + " -> "
                                + entry.getValue());

                rank++;

                if (rank > 10)
                    break;
            }

            // Generate Report
            FileWriter writer =
                    new FileWriter("../reports/covid_report.txt");

            writer.write("COVID-19 ANALYSIS REPORT\n\n");

            writer.write("Country : India\n");
            writer.write("Latest Date : " + latestDate + "\n");
            writer.write("Total Cases : " + totalCases + "\n");
            writer.write("Total Deaths : " + totalDeaths + "\n");
            writer.write(String.format(
                    "Recovery Rate : %.2f%%\n\n",
                    recoveryRate));

            writer.write("TOP 10 COUNTRIES BY TOTAL CASES\n\n");

            int reportRank = 1;

            for (Map.Entry<String, String> entry : list) {

                writer.write(
                        reportRank + ". "
                                + entry.getKey()
                                + " -> "
                                + entry.getValue()
                                + "\n");

                reportRank++;

                if (reportRank > 10)
                    break;
            }

            writer.close();

            System.out.println(
                    "\nReport Generated Successfully!");

            // Country Search
            System.out.println(
                    "\n========== COUNTRY SEARCH ==========");
            System.out.print("Enter Country Name: ");

            String searchCountry = sc.nextLine();

            BufferedReader br2 =
                    new BufferedReader(
                            new FileReader(filePath));

            br2.readLine();

            String searchDate = "";
            String searchCases = "";
            String searchDeaths = "";

            while ((line = br2.readLine()) != null) {

                String[] data = line.split(",");

                if (data.length > 7
                        && data[2].equalsIgnoreCase(searchCountry)) {

                    if (!data[4].trim().isEmpty()) {

                        searchDate = data[3];
                        searchCases = data[4];
                        searchDeaths = data[7];
                    }
                }
            }

            br2.close();

            if (!searchCases.isEmpty()) {

                double c = Double.parseDouble(searchCases);
                double d = Double.parseDouble(searchDeaths);

                double recovery =
                        ((c - d) / c) * 100;

                System.out.println("\nCountry : "
                        + searchCountry);

                System.out.println("Latest Date : "
                        + searchDate);

                System.out.println("Total Cases : "
                        + searchCases);

                System.out.println("Total Deaths : "
                        + searchDeaths);

                System.out.printf(
                        "Recovery Rate : %.2f%%\n",
                        recovery);

            } else {

                System.out.println("Country not found!");
            }

            sc.close();

        } catch (IOException e) {

            System.out.println("Error reading file!");
            e.printStackTrace();
        }
    }
}