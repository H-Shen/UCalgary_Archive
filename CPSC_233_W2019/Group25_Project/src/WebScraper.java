import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created to scrape courses data off of https://www.ucalgary.ca/pubs/calendar/current/index.htm, and upload to the database
 *
 * @author Group 25
 * @date 2019/04/08
 * @since interactive demo
 */
public class WebScraper {

    /**
     * @Fields An array stores all websites of course descriptions of different departments of UCalgary.
     */
    private static final String[] URL_ARRAY = {
            "https://www.ucalgary.ca/pubs/calendar/current/academic-writing.html",
            "https://www.ucalgary.ca/pubs/calendar/current/accounting.html",
            "https://www.ucalgary.ca/pubs/calendar/current/actuarial-science.html",
            "https://www.ucalgary.ca/pubs/calendar/current/african-studies.html",
            "https://www.ucalgary.ca/pubs/calendar/current/american-sign-language.html",
            "https://www.ucalgary.ca/pubs/calendar/current/anthropology.html",
            "https://www.ucalgary.ca/pubs/calendar/current/arabic-language-and-muslim-culture.html",
            "https://www.ucalgary.ca/pubs/calendar/current/archaeology.html",
            "https://www.ucalgary.ca/pubs/calendar/current/architectural-studies.html",
            "https://www.ucalgary.ca/pubs/calendar/current/art-history.html",
            "https://www.ucalgary.ca/pubs/calendar/current/art.html",
            "https://www.ucalgary.ca/pubs/calendar/current/arts-and-science-honours-academy.html",
            "https://www.ucalgary.ca/pubs/calendar/current/astronomy.html",
            "https://www.ucalgary.ca/pubs/calendar/current/astrophysics.html",
            "https://www.ucalgary.ca/pubs/calendar/current/biochemistry.html",
            "https://www.ucalgary.ca/pubs/calendar/current/biology.html",
            "https://www.ucalgary.ca/pubs/calendar/current/biomedical-engineering.html",
            "https://www.ucalgary.ca/pubs/calendar/current/biostatistics.html",
            "https://www.ucalgary.ca/pubs/calendar/current/business-and-environment.html",
            "https://www.ucalgary.ca/pubs/calendar/current/business-technology-management.html",
            "https://www.ucalgary.ca/pubs/calendar/current/canadian-studies.html",
            "https://www.ucalgary.ca/pubs/calendar/current/cellular-molecular-and-microbial-biology.html",
            "https://www.ucalgary.ca/pubs/calendar/current/central-and-east-european-studies.html",
            "https://www.ucalgary.ca/pubs/calendar/current/chemical-engineering.html",
            "https://www.ucalgary.ca/pubs/calendar/current/chemistry.html",
            "https://www.ucalgary.ca/pubs/calendar/current/chinese.html",
            "https://www.ucalgary.ca/pubs/calendar/current/civil-engineering.html",
            "https://www.ucalgary.ca/pubs/calendar/current/communication-and-culture.html",
            "https://www.ucalgary.ca/pubs/calendar/current/communication-media-studies.html",
            "https://www.ucalgary.ca/pubs/calendar/current/community-health-sciences-mdch.html",
            "https://www.ucalgary.ca/pubs/calendar/current/community-rehabilitation.html",
            "https://www.ucalgary.ca/pubs/calendar/current/computational-media-design.html",
            "https://www.ucalgary.ca/pubs/calendar/current/computer-engineering.html",
            "https://www.ucalgary.ca/pubs/calendar/current/computer-science.html",
            "https://www.ucalgary.ca/pubs/calendar/current/cooperative-education.html",
            "https://www.ucalgary.ca/pubs/calendar/current/dance-education.html",
            "https://www.ucalgary.ca/pubs/calendar/current/dance.html",
            "https://www.ucalgary.ca/pubs/calendar/current/data-science.html",
            "https://www.ucalgary.ca/pubs/calendar/current/development-studies.html",
            "https://www.ucalgary.ca/pubs/calendar/current/drama.html",
            "https://www.ucalgary.ca/pubs/calendar/current/earth-science.html",
            "https://www.ucalgary.ca/pubs/calendar/current/east-asian-language-studies.html",
            "https://www.ucalgary.ca/pubs/calendar/current/east-asian-studies.html",
            "https://www.ucalgary.ca/pubs/calendar/current/ecology.html",
            "https://www.ucalgary.ca/pubs/calendar/current/economics.html",
            "https://www.ucalgary.ca/pubs/calendar/current/education-bridge-to-teaching-edbt.html",
            "https://www.ucalgary.ca/pubs/calendar/current/education-educ.html",
            "https://www.ucalgary.ca/pubs/calendar/current/educational-psychology.html",
            "https://www.ucalgary.ca/pubs/calendar/current/educational-research.html",
            "https://www.ucalgary.ca/pubs/calendar/current/electrical-engineering.html",
            "https://www.ucalgary.ca/pubs/calendar/current/energy-and-environment-engineering.html",
            "https://www.ucalgary.ca/pubs/calendar/current/energy-and-environmental-systems.html",
            "https://www.ucalgary.ca/pubs/calendar/current/energy-engineering.html",
            "https://www.ucalgary.ca/pubs/calendar/current/energy-management.html",
            "https://www.ucalgary.ca/pubs/calendar/current/engineering.html",
            "https://www.ucalgary.ca/pubs/calendar/current/english.html",
            "https://www.ucalgary.ca/pubs/calendar/current/entrepreneurship-and-innovation.html",
            "https://www.ucalgary.ca/pubs/calendar/current/environmental-design-architecture.html",
            "https://www.ucalgary.ca/pubs/calendar/current/environmental-design-landscape.html",
            "https://www.ucalgary.ca/pubs/calendar/current/environmental-design-planning-evdp.html",
            "https://www.ucalgary.ca/pubs/calendar/current/environmental-design.html",
            "https://www.ucalgary.ca/pubs/calendar/current/environmental-engineering.html",
            "https://www.ucalgary.ca/pubs/calendar/current/environmental-science.html",
            "https://www.ucalgary.ca/pubs/calendar/current/film.html",
            "https://www.ucalgary.ca/pubs/calendar/current/finance.html",
            "https://www.ucalgary.ca/pubs/calendar/current/fine-arts.html",
            "https://www.ucalgary.ca/pubs/calendar/current/french.html",
            "https://www.ucalgary.ca/pubs/calendar/current/geography.html",
            "https://www.ucalgary.ca/pubs/calendar/current/geology.html",
            "https://www.ucalgary.ca/pubs/calendar/current/geomatics-engineering.html",
            "https://www.ucalgary.ca/pubs/calendar/current/geophysics.html",
            "https://www.ucalgary.ca/pubs/calendar/current/german.html",
            "https://www.ucalgary.ca/pubs/calendar/current/greek-and-roman-studies.html",
            "https://www.ucalgary.ca/pubs/calendar/current/greek.html",
            "https://www.ucalgary.ca/pubs/calendar/current/health-and-society.html",
            "https://www.ucalgary.ca/pubs/calendar/current/history.html",
            "https://www.ucalgary.ca/pubs/calendar/current/indigenous-languages-indl.html",
            "https://www.ucalgary.ca/pubs/calendar/current/indigenous-studies.html",
            "https://www.ucalgary.ca/pubs/calendar/current/information-security-isec.html",
            "https://www.ucalgary.ca/pubs/calendar/current/innovation.html",
            "https://www.ucalgary.ca/pubs/calendar/current/international-foundations-program-business.html",
            "https://www.ucalgary.ca/pubs/calendar/current/international-foundations-program-engineering.html",
            "https://www.ucalgary.ca/pubs/calendar/current/international-foundations-program.html",
            "https://www.ucalgary.ca/pubs/calendar/current/international-relations.html",
            "https://www.ucalgary.ca/pubs/calendar/current/internship.html",
            "https://www.ucalgary.ca/pubs/calendar/current/interprofessional-health-education.html",
            "https://www.ucalgary.ca/pubs/calendar/current/italian.html",
            "https://www.ucalgary.ca/pubs/calendar/current/japanese.html",
            "https://www.ucalgary.ca/pubs/calendar/current/kinesiology.html",
            "https://www.ucalgary.ca/pubs/calendar/current/language.html",
            "https://www.ucalgary.ca/pubs/calendar/current/latin-american-studies.html",
            "https://www.ucalgary.ca/pubs/calendar/current/latin.html",
            "https://www.ucalgary.ca/pubs/calendar/current/law-and-society.html",
            "https://www.ucalgary.ca/pubs/calendar/current/law.html",
            "https://www.ucalgary.ca/pubs/calendar/current/linguistics.html",
            "https://www.ucalgary.ca/pubs/calendar/current/management-studies.html",
            "https://www.ucalgary.ca/pubs/calendar/current/manufacturing-engineering.html",
            "https://www.ucalgary.ca/pubs/calendar/current/marine-science.html",
            "https://www.ucalgary.ca/pubs/calendar/current/marketing.html",
            "https://www.ucalgary.ca/pubs/calendar/current/mathematics.html",
            "https://www.ucalgary.ca/pubs/calendar/current/mechanical-engineering.html",
            "https://www.ucalgary.ca/pubs/calendar/current/medical-graduate-education.html",
            "https://www.ucalgary.ca/pubs/calendar/current/medical-physics.html",
            "https://www.ucalgary.ca/pubs/calendar/current/medical-science.html",
            "https://www.ucalgary.ca/pubs/calendar/current/medicine.html",
            "https://www.ucalgary.ca/pubs/calendar/current/museum-and-heritage-studies.html",
            "https://www.ucalgary.ca/pubs/calendar/current/music-education.html",
            "https://www.ucalgary.ca/pubs/calendar/current/music-performance.html",
            "https://www.ucalgary.ca/pubs/calendar/current/music.html",
            "https://www.ucalgary.ca/pubs/calendar/current/nanoscience.html",
            "https://www.ucalgary.ca/pubs/calendar/current/neuroscience.html",
            "https://www.ucalgary.ca/pubs/calendar/current/nursing.html",
            "https://www.ucalgary.ca/pubs/calendar/current/operations-management.html",
            "https://www.ucalgary.ca/pubs/calendar/current/organizational-behaviour-and-human-resources.html",
            "https://www.ucalgary.ca/pubs/calendar/current/petroleum-engineering.html",
            "https://www.ucalgary.ca/pubs/calendar/current/philosophy.html",
            "https://www.ucalgary.ca/pubs/calendar/current/physical-education.html",
            "https://www.ucalgary.ca/pubs/calendar/current/physics.html",
            "https://www.ucalgary.ca/pubs/calendar/current/plant-biology.html",
            "https://www.ucalgary.ca/pubs/calendar/current/professional-land-management.html",
            "https://www.ucalgary.ca/pubs/calendar/current/public-policy-ppol.html",
            "https://www.ucalgary.ca/pubs/calendar/current/real-estate-studies.html",
            "https://www.ucalgary.ca/pubs/calendar/current/religious-studies.html",
            "https://www.ucalgary.ca/pubs/calendar/current/risk-management-and-insurance.html",
            "https://www.ucalgary.ca/pubs/calendar/current/romance-studies.html",
            "https://www.ucalgary.ca/pubs/calendar/current/russian.html",
            "https://www.ucalgary.ca/pubs/calendar/current/school-of-creative-and-performing-arts.html",
            "https://www.ucalgary.ca/pubs/calendar/current/science.html",
            "https://www.ucalgary.ca/pubs/calendar/current/slavic.html",
            "https://www.ucalgary.ca/pubs/calendar/current/social-work.html",
            "https://www.ucalgary.ca/pubs/calendar/current/sociology.html",
            "https://www.ucalgary.ca/pubs/calendar/current/software-engineering-for-engineers.html",
            "https://www.ucalgary.ca/pubs/calendar/current/software-engineering.html",
            "https://www.ucalgary.ca/pubs/calendar/current/south-asian-studies.html",
            "https://www.ucalgary.ca/pubs/calendar/current/space-physics.html",
            "https://www.ucalgary.ca/pubs/calendar/current/spanish.html",
            "https://www.ucalgary.ca/pubs/calendar/current/statistics.html",
            "https://www.ucalgary.ca/pubs/calendar/current/strategic-studies.html",
            "https://www.ucalgary.ca/pubs/calendar/current/strategy-and-global-management.html",
            "https://www.ucalgary.ca/pubs/calendar/current/supply-chain-management.html",
            "https://www.ucalgary.ca/pubs/calendar/current/sustainability-studies-sust.html",
            "https://www.ucalgary.ca/pubs/calendar/current/sustainable-energy-development.html",
            "https://www.ucalgary.ca/pubs/calendar/current/term-abroad-program.html",
            "https://www.ucalgary.ca/pubs/calendar/current/tourism-management.html",
            "https://www.ucalgary.ca/pubs/calendar/current/transportation-studies.html",
            "https://www.ucalgary.ca/pubs/calendar/current/university-exchange.html",
            "https://www.ucalgary.ca/pubs/calendar/current/urban-studies.html",
            "https://www.ucalgary.ca/pubs/calendar/current/veterinary-medicine.html",
            "https://www.ucalgary.ca/pubs/calendar/current/wellbeing.html",
            "https://www.ucalgary.ca/pubs/calendar/current/womens-studies.html",
            "https://www.ucalgary.ca/pubs/calendar/current/zoology.html"
    };

    /**
     * Loops through the array of URLs and grabs all elements using JSOUP module
     * Then prints off information into a JSON file
     *
     * @date 2019/04/08
     */
    public static void main(String[] args) {

        System.out.println("running...");
        Document document;
        try {

            //Get Document object after parsing the html" from given url.
            for (String s : URL_ARRAY) {

                document = Jsoup.connect(s).get();
                Elements courseName = document.select(".link-text");

                String tempName = courseName.get(0).text();
                String name     = tempName.substring(0, 4);

                Elements courseNumber      = document.select(".course-code");
                Elements courseDescription = document.select(".course-desc");
                Elements courseCredits     = document.select((".course-hours"));
                Elements antirequisites    = document.select(".course-antireq");
                Elements prerequisites     = document.select(".course-prereq");
                Elements corequisites      = document.select(".course-coreq");
                Elements canBeRepeated     = document.select(".course-repeat");

                writeToPreparedStatement(name, corequisites, courseNumber, courseDescription, courseCredits, antirequisites,
                        prerequisites, canBeRepeated);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("...done");
    }

    /**
     * The method creates a prepared statement out of the data parsed from the web scraper in a way that allows it to
     * be inserted into the sql database
     *
     * @param connection        a Connection object
     * @param courseId          the course id
     * @param courseTitle       the course title
     * @param courseDescription the course description
     * @param courseCredits     the course credits
     * @param preq              the prerequisites of the course
     * @param anti              the anti-requisites of the course
     * @param core              the core-requisites of the course
     * @param canBeRepeated     a boolean property which shows if the course can be repeatedly taken for GPA
     * @return a prepared statement to insert into Sql database
     * @date 2019/04/08
     */
    private static PreparedStatement helperFunc(Connection connection,
                                                String courseId, String courseTitle,
                                                String courseDescription, String courseCredits, String preq,
                                                String anti, String core, String canBeRepeated)
            throws SQLException {

        String            sql               = "INSERT INTO COURSES VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, courseId);
        preparedStatement.setString(2, courseTitle);
        preparedStatement.setString(3, courseDescription);
        preparedStatement.setString(4, courseCredits);
        preparedStatement.setString(5, preq);
        preparedStatement.setString(6, anti);
        preparedStatement.setString(7, core);
        preparedStatement.setString(8, canBeRepeated);
        return preparedStatement;
    }

    /**
     * The takes the data parsed from the web scraper and prints it to a prepared statement to format correctly to be
     * inserted into database
     *
     * @param name              the course title
     * @param corequisites      the core-requisites of the course
     * @param courseNumber      the course number
     * @param courseDescription the course description
     * @param courseCredits     the course credits
     * @param antirequisites    the anti-requisites of the course
     * @param prerequisites     the prerequisites of the course
     * @param canBeRepeated     a boolean property which shows if the course can be repeatedly taken for GPA
     * @date 2019/04/08
     */
    private static void writeToPreparedStatement(String name, Elements corequisites, Elements courseNumber,
                                                 Elements courseDescription,
                                                 Elements courseCredits, Elements antirequisites, Elements prerequisites, Elements canBeRepeated) {
        // initialization
        int nameNumber  = 1;
        int titleNumber = 2;

        for (int i = 0; i < courseDescription.size(); i++) {


            try (Connection connection = Database.createConnection(Database.COURSES_ORIGINAL, false);
                 PreparedStatement statement = helperFunc(connection,
                         name + courseNumber.get(nameNumber).text(),
                         courseNumber.get(titleNumber).text(),
                         courseDescription.get(i).text(),
                         courseCredits.get(i).text(),
                         prerequisites.get(i).text(),
                         antirequisites.get(i).text(),
                         corequisites.get(i).text(),
                         canBeRepeated.get(i).text()
                 )
            ) {
                // indicator
                System.out.println(name + courseNumber.get(nameNumber).text() + " obtained!");

                statement.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // adjust the offset
            nameNumber += 3;
            titleNumber += 3;
        }
    }
}
