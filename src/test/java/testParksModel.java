import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import model.NetUtils;
import model.Records.*;

import java.util.HashSet;
import java.util.List;
import model.ParksModel;
import com.fasterxml.jackson.databind.ObjectMapper;

public class testParksModel {

    ObjectMapper objectMapper = new ObjectMapper();
    Park park1;
    Park park2;
    ParksModel model;

    @BeforeEach
    public void setUP() {
        model = new ParksModel();
    }

    @Test
    public void updateDB() {
        String response = NetUtils.getParksByState("CA");
        try {
            List<Park> parkList = ParksModel.deserializeResponse(response);
            assertTrue(model.updateDB("CA"));
            assertEquals(parkList, model.getParkList());
        } catch (Exception e) {
            System.out.println("JSON parse exception" + e);
        } 

    }

    @Test
    public void deserializeList() {
        String json = NetUtils.getParksByState("CA");

        try {
            List<Park> actual = ParksModel.deserializeResponse(json);
            assertNotNull(actual);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Test
    public void deserializeSubList() {
        
        String json = """
        {

        "total":"17",
        "limit":"20",
        "start":"0",  
        "data":[

        {

        "id":"98C8117C-EDE9-4A7C-9354-5AD0DEFDB848"

        ,"url":

        "https://www.nps.gov/ebla/index.htm"

        ,"fullName":"Ebey's Landing National Historical Reserve"      

        ,"parkCode":"ebla"

        ,"description":"This stunning landscape on the Salish Sea, with its rich farmland and promising seaport, lured the earliest American pioneers north of the Columbia River to Ebey’s Landing. Today Ebey’s Landing National Historical Reserve preserves the historical, agricultural and cultural traditions of both Native and Euro-American – while offering spectacular opportunities for recreation."

        ,"latitude":

        "48.21403036" 

        ,"longitude":

        "-122.6877213" 

        ,"latLong":"lat:48.21403036, long:-122.6877213"

        ,"activities":[{"id":"13A57703-BB1A-41A2-94B8-53B692EB7238","name":"Astronomy"},{"id":"D37A0003-8317-4F04-8FB0-4CF0A272E195","name":"Stargazing"},{"id":"7CE6E935-F839-4FEC-A63E-052B1DEF39D2","name":"Biking"},{"id":"299CB934-88DC-474F-A33D-E43E29A149C2","name":"Mountain Biking"},{"id":"8D778629-F603-4C50-A121-6F4BB2FE2C4B","name":"Road Biking"},{"id":"071BA73C-1D3C-46D4-A53C-00D5602F7F0E","name":"Boating"},{"id":"BB0B8CD0-BF4C-4517-9980-CFE2D149C7B4","name":"Sailing"},{"id":"A59947B7-3376-49B4-AD02-C0423E08C5F7","name":"Camping"},{"id":"9159DF0F-951D-4AAE-9987-CEB3CE2A9ADA","name":"Car or Front Country Camping"},{"id":"7CFF5F03-5ECC-4F5A-8572-75D1F0976C0C","name":"Group Camping"},{"id":"AE42B46C-E4B7-4889-A122-08FE180371AE","name":"Fishing"},{"id":"17411C8D-5769-4D0F-ABD1-52ED03840C18","name":"Saltwater Fishing"},{"id":"1DFACD97-1B9C-4F5A-80F2-05593604799E","name":"Food"},{"id":"E53E1320-9B17-41DC-86A5-37EB7D622572","name":"Dining"},{"id":"C6D3230A-2CEA-4AFE-BFF3-DC1E2C2C4BB4","name":"Picnicking"},{"id":"B33DC9B6-0B7D-4322-BAD7-A13A34C584A3","name":"Guided Tours"},{"id":"A0631906-9672-4583-91DE-113B93DB6B6E","name":"Self-Guided Tours - Walking"},{"id":"C7D5A145-F8EB-4C37-9E92-2F6C6206B196","name":"Self-Guided Tours - Auto"},{"id":"BFF8C027-7C8F-480B-A5F8-CD8CE490BFBA","name":"Hiking"},{"id":"45261C0A-00D8-4C27-A1F8-029F933A0D34","name":"Front-Country Hiking"},{"id":"0307955A-B65C-4CE4-A780-EB36BAAADF0B","name":"Horse Trekking"},{"id":"1886DA47-0AEC-4568-B9C2-8E9BC316AAAC","name":"Horseback Riding"},{"id":"4D224BCA-C127-408B-AC75-A51563C42411","name":"Paddling"},{"id":"21DB3AFC-8AAC-4665-BC1F-7198C0685983","name":"Canoeing"},{"id":"256543C7-4322-48B3-8978-765E89AA9431","name":"Canoe or Kayak Camping"},{"id":"F353A9ED-4A08-456E-8DEC-E61974D0FEB6","name":"Kayaking"},{"id":"B3EF12AF-D951-419E-BD3C-6B36CBCC1E16","name":"Stand Up Paddleboarding"},{"id":"DF4A35E0-7983-4A3E-BC47-F37B872B0F25","name":"Junior Ranger Program"},{"id":"42CF4021-8524-428E-866A-D33097A4A764","name":"SCUBA Diving"},{"id":"0B685688-3405-4E2A-ABBA-E3069492EC50","name":"Wildlife Watching"},{"id":"5A2C91D1-50EC-4B24-8BED-A2E11A1892DF","name":"Birdwatching"},{"id":"C8F98B28-3C10-41AE-AA99-092B3B398C43","name":"Museum Exhibits"},{"id":"24380E3F-AD9D-4E38-BF13-C8EEB21893E7","name":"Shopping"}]
        ,"topics":[{"id":"69693007-2DF2-4EDE-BB3B-A25EBA72BDF5","name":"Architecture and Building"},{"id":"227D2677-28CA-4CBF-997F-61108975A497","name":"Asian American Heritage"},{"id":"607D41B0-F830-4C07-A557-BCEF880A3929","name":"Burial, Cemetery and Gravesite"},{"id":"7F12224B-217A-4B07-A4A2-636B1CE7F221","name":"Colonization and Listtlement"},{"id":"1170EEB6-5070-4760-8E7D-FF1A98272FAD","name":"Commerce"},{"id":"A010EADF-78B8-4526-B0A4-70B0E0B3470E","name":"Trade"},{"id":"12EA2B56-17EC-410A-A10D-BFBA87A0669B","name":"Explorers and Expeditions"},{"id":"1F833C99-A75D-4F9E-9256-B96523485466","name":"Farming and Agriculture"},{"id":"988B4AFC-F478-4673-B66D-E6BDB0CCFF35","name":"Forts"},{"id":"AF4F1CDF-E6C4-4886-BA91-8BC887DC2793","name":"Landscape Design"},{"id":"4C9D4777-A9DA-47D1-A0B9-F4A3C98BC1B3","name":"Maritime"},{"id":"123CE28E-0EFA-4330-8E6E-EEFF3D7BF772","name":"Coastal Defenses"},{"id":"7424754D-EB8B-4608-A69A-50D44992931B","name":"Maritime - Military"},{"id":"263BAC6E-4DEC-47E4-909D-DA8AD351E06E","name":"Lighthouses"},{"id":"69E16062-0E4F-4DE0-91FB-E4EDB2484572","name":"Migrations"},{"id":"3B0D607D-9933-425A-BFA0-21529AC4734C","name":"Military"},{"id":"A1BAF33E-EA84-4608-A888-4CEE9541F027","name":"Native American Heritage"},{"id":"C9C749E3-39C3-45F7-BCC5-9A609E30AA05","name":"Westward Expansion"},{"id":"D1722DD1-E314-4B6D-8116-DED86305C4A4","name":"Homesteading"},{"id":"0D00073E-18C3-46E5-8727-2F87B112DDC6","name":"Animals"},{"id":"957EF2BD-AC6C-4B7B-BD9A-87593ADC6691","name":"Birds"},{"id":"1608649A-E7D7-4529-BD83-074C90F9FB68","name":"Fish"},{"id":"46FC5CBD-9AD5-48F1-B4DA-1357031B1D2E","name":"Coasts, Islands and Atolls"},{"id":"94262026-92F5-48E9-90EF-01CEAEFBA4FF","name":"Grasslands"},{"id":"1AEDC86F-5792-487F-8CDF-9E92CAB97ACE","name":"Prairies"},{"id":"A7359FC4-DAD8-45F5-AF15-7FF62F816ED3","name":"Night Sky"},{"id":"E06F3C94-AC8A-4B1C-A247-8EBA8910D5EE","name":"Astronomy"},{"id":"B5E62BF4-5AE6-4100-8DE1-66652943FAEC","name":"Aurora Borealis"},{"id":"0E6D8503-CB65-467F-BCD6-C6D9E28A4E0B","name":"Oceans"},{"id":"980D1693-65BF-4F29-8182-7BAC9796E605","name":"Whales"},{"id":"78E4F4AC-AF97-435A-8C2C-7FB8D67516ED","name":"Unique Species"},{"id":"996BEDAE-CB23-4003-B008-3A1F46A72263","name":"Rare"}]
        ,"states":"WA"

        ,"contacts":{"phoneNumbers":[{"phoneNumber":"3606786084","description":"","extension":"","type":"Voice"}],"emailAddresses":[{"description":"","emailAddress":"clifford_deloreon@nps.gov"}]}
        ,"entranceFees":[]
        ,"entrancePasses":[]
        ,"fees":[]
        ,"directionsInfo":"Ebey's Landing National Historical Reserve is on Whidbey Island in the Puget Sound. The island is easily accessible from the mainland by vehicle via Washington State Route 20 from Burlington and aboard the Washington State Ferries from either Mukilteo or Port Townsend."

        ,"directionsUrl":"http://www.nps.gov/ebla/planyourvisit/directions.htm"

        ,"operatingHours":[{"exceptions":[],"description":"The Reserve is over 80% privately owned, contains three Washington State Parks, and the historic town of Coupeville. There are no hours or fees for visiting the Reserve, but individual business and parks within the Reserve set their own hours.","standardHours":{"wednesday":"All Day","monday":"All Day","thursday":"All Day","sunday":"All Day","tuesday":"All Day","friday":"All Day","saturday":"All Day"},"name":"Ebey's Landing National Historical Reserve"},{"exceptions":[],"description":"The historic Jacob & Sarah Ebey House and the Ebey Blockhouse are open to the public during the summer months, Thursdays through Sundays, from 10am to 4pm.","standardHours":{"wednesday":"Closed","monday":"Closed","thursday":"10:00AM - 4:00PM","sunday":"10:00AM - 4:00PM","tuesday":"Closed","friday":"10:00AM - 4:00PM","saturday":"10:00AM - 4:00PM"},"name":"Jacob and Sarah Ebey House"},{"exceptions":[],"description":"The administrative offices located in the Cottage at Sunnyside are open from 9 to 5,  Monday through Friday.","standardHours":{"wednesday":"9:00AM - 5:00PM","monday":"9:00AM - 5:00PM","thursday":"9:00AM - 5:00PM","sunday":"Closed","tuesday":"9:00AM - 5:00PM","friday":"9:00AM - 5:00PM","saturday":"Closed"},"name":"Administrative Office"}]
        ,"addresses":[{"postalCode":"98239","city":"Coupeville","stateCode":"WA","countryCode":"US","provinceTerritoryCode":"","line1":"162 Cemetery Road","type":"Physical","line3":"","line2":""},{"postalCode":"98239","city":"Coupeville","stateCode":"WA","countryCode":"US","provinceTerritoryCode":"","line1":"PO Box 774","type":"Mailing","line3":"","line2":""}]
        ,"images":[{"credit":"NPS Photo / H. Richards","title":"View from the Bluff Overlook","altText":"View from the Bluff Overlook","caption":"The Bluff Overlook on the Bluff Trail offers spectacular views of the Straight of Juan de Fuca","url":"https://www.nps.gov/common/uploads/structured_data/3C84BC00-1DD8-B71B-0BD2CA9CA44675E9.jpg"},{"credit":"NPS Photo / H. Richards","title":"Sunrise over Admiralty Bay","altText":"Sunrise over Admiralty Bay","caption":"Admiralty Bay in Fort Casey State Park offers spectacular views of Mt Rainier and the Olympic Mountains.","url":"https://www.nps.gov/common/uploads/structured_data/3C84BDD7-1DD8-B71B-0B3517BBE2AFC768.jpg"},{"credit":"NPS Photo / H. Richards","title":"Mt Baker and the historic Smith Barn","altText":"Mt Baker and the historic Smith Barn","caption":"The views from the prairie overlook tell a story of farming and community that stretches back for centuries.","url":"https://www.nps.gov/common/uploads/structured_data/3C84BF6A-1DD8-B71B-0B478117375417E0.jpg"},{"credit":"NPS Photo / H. Richards","title":"Sunrise Over a Prairie Farm","altText":"Sunrise over the prairie.","caption":"Ebey's Landing National Historical Reserve exists to preserve a working rural farm community.","url":"https://www.nps.gov/common/uploads/structured_data/3C84C127-1DD8-B71B-0B41F30944B7EABF.jpg"},{"credit":"NPS Photo / H. Richards","title":"Historic Ferry House","altText":"Historic Ferry House","caption":"The Ferry House sits in the heart of the Reserve, a testament to the community that calls this place home.","url":"https://www.nps.gov/common/uploads/structured_data/3C84C278-1DD8-B71B-0B90C49906FF109C.jpg"}]
        ,"weatherInfo":"Fall and Winter often bring blustery and rainy weather, and while winter storm watching might be fun from the many beaches within the Reserve, it's important to watch for high waves and logs coming ashore. Always dress appropriately for the weather, which can change suddenly from warm to chilly very quickly."

        ,"name":"Ebey's Landing"

        ,"designation":"National Historical Reserve"

        ,"multimedia":[]
        ,"relevanceScore":1.0

        }
        ,
        {

        "id":"B13AB831-6806-4CBA-A93D-4E2FCC39F6F7"

        ,"url":

        "https://www.nps.gov/fova/index.htm"

        ,"fullName":"Fort Vancouver National Historic Site"      

        ,"parkCode":"fova"

        ,"description":"Located on the north bank of the Columbia River, in sight of snowy mountain peaks and a vibrant urban landscape, this park has a rich cultural past. From a frontier fur trading post, to a powerful military legacy, the magic of flight, and the origin of the American Pacific Northwest, history is shared at four unique sites. Discover stories of transition, settlement, conflict, and community."

        ,"latitude":

        "45.62234841" 

        ,"longitude":

        "-122.6617043" 

        ,"latLong":"lat:45.62234841, long:-122.6617043"

        ,"activities":[{"id":"09DF0950-D319-4557-A57E-04CD2F63FF42","name":"Arts and Culture"},{"id":"C59E231D-55FC-4B37-BC5B-FF76383951B5","name":"Craft Demonstrations"},{"id":"FAED7F97-3474-4C21-AB42-FB0768A2F826","name":"Cultural Demonstrations"},{"id":"7CE6E935-F839-4FEC-A63E-052B1DEF39D2","name":"Biking"},{"id":"B33DC9B6-0B7D-4322-BAD7-A13A34C584A3","name":"Guided Tours"},{"id":"B204DE60-5A24-43DD-8902-C81625A09A74","name":"Living History"},{"id":"A8650547-1A30-4222-86C3-A7660A829670","name":"Reenactments"},{"id":"237A1662-6018-4115-ABD1-B8CCF827E703","name":"Historic Weapons Demonstration"},{"id":"DF4A35E0-7983-4A3E-BC47-F37B872B0F25","name":"Junior Ranger Program"},{"id":"7779241F-A70B-49BC-86F0-829AE332C708","name":"Playground"},{"id":"0B685688-3405-4E2A-ABBA-E3069492EC50","name":"Wildlife Watching"},{"id":"0C0D142F-06B5-4BE1-8B44-491B90F93DEB","name":"Park Film"},{"id":"C8F98B28-3C10-41AE-AA99-092B3B398C43","name":"Museum Exhibits"},{"id":"24380E3F-AD9D-4E38-BF13-C8EEB21893E7","name":"Shopping"},{"id":"467DC8B8-0B7D-436D-A026-80A22358F615","name":"Bookstore and Park Store"}]
        ,"topics":[{"id":"69693007-2DF2-4EDE-BB3B-A25EBA72BDF5","name":"Architecture and Building"},{"id":"28AEAE85-9DDA-45B6-981B-1CFCDCC61E14","name":"African American Heritage"},{"id":"7F81A0CB-B91F-4896-B9A5-41BE9A54A27B","name":"Archeology"},{"id":"227D2677-28CA-4CBF-997F-61108975A497","name":"Asian American Heritage"},{"id":"B912363F-771C-4098-BA3A-938DF38A9D7E","name":"Aviation"},{"id":"7F12224B-217A-4B07-A4A2-636B1CE7F221","name":"Colonization and Listtlement"},{"id":"1170EEB6-5070-4760-8E7D-FF1A98272FAD","name":"Commerce"},{"id":"A010EADF-78B8-4526-B0A4-70B0E0B3470E","name":"Trade"},{"id":"988B4AFC-F478-4673-B66D-E6BDB0CCFF35","name":"Forts"},{"id":"E0AB480F-3A94-4DC1-8B21-9555F2C59B32","name":"LGB American Heritage"},{"id":"3B0D607D-9933-425A-BFA0-21529AC4734C","name":"Military"},{"id":"97CCB419-196C-4B95-BB3A-621458F78415","name":"US Army"},{"id":"A1BAF33E-EA84-4608-A888-4CEE9541F027","name":"Native American Heritage"},{"id":"272549CF-EAA4-4603-8E20-FBE1AAB391B7","name":"Pacific Islander Heritage"},{"id":"7DA81DAB-5045-4953-9C20-36590AD9FA95","name":"Women's History"},{"id":"0D00073E-18C3-46E5-8727-2F87B112DDC6","name":"Animals"}]
        ,"states":"OR,WA"

        ,"contacts":{"phoneNumbers":[{"phoneNumber":"3608166230","description":"","extension":"","type":"Voice"}],"emailAddresses":[{"description":"","emailAddress":"fova_information@nps.gov"}]}
        ,"entranceFees":[{"cost":"10.00","description":"Day use/entrance fees are required for visitors ages 16 and up to access reconstructed Fort Vancouver. Federal Interagency Passes are accepted. There is no day use/entrance fee for visitors accessing the Visitor Center, Pearson Air Museum, the Barclay House, and McLoughlin House.","title":"Entrance - Per Person"}]
        ,"entrancePasses":[{"cost":"35.00","description":"Fort Vancouver National Historic Site offers an annual pass that covers entrance fees for 4 visitors ages 16 and up. Visitors ages 15 and under are free. This pass can be purchased at the gates of Fort Vancouver or at Recreation.gov.","title":"Annual Entrance - Park"}]
        ,"fees":[]
        ,"directionsInfo":"From I-5, take the Mill Plain Boulevard exit (Exit 1-C) and head east. Turn south onto Fort Vancouver Way. At the traffic circle, go east on Evergreen Boulevard and follow signs to the Fort Vancouver Visitor Center. From I-205, go west on Highway 14 about six miles, then take I-5 north. From I-5, take the Mill Plain Boulevard exit (Exit 1-C) and head east. At the traffic circle, go east on Evergreen Boulevard and follow signs to the Fort Vancouver Visitor Center."

        ,"directionsUrl":"http://www.nps.gov/fova/planyourvisit/directions.htm"

        ,"operatingHours":[{"exceptions":[{"exceptionHours":{"wednesday":"Closed","monday":"Closed","thursday":"Closed","sunday":"Closed","tuesday":"Closed","friday":"Closed","saturday":"Closed"},"startDate":"2025-11-27","name":"Thanksgiving Day","endDate":"2025-11-27"},{"exceptionHours":{"wednesday":"Closed","monday":"Closed","thursday":"Closed","sunday":"Closed","tuesday":"Closed","friday":"Closed","saturday":"Closed"},"startDate":"2025-12-22","name":"Winter Season Closure","endDate":"2026-01-06"},{"exceptionHours":{},"startDate":"2025-12-24","name":"Christmas Eve & Christmas Day","endDate":"2025-12-25"},{"exceptionHours":{},"startDate":"2025-12-31","name":"New Year's Eve & New Year's Day","endDate":"2026-01-01"}],"description":"Standard operating hours for Vancouver, Washington facilities are Tuesday through Saturday, 9:00 a.m. to 4:00 p.m.","standardHours":{"wednesday":"9:00AM - 4:00PM","monday":"Closed","thursday":"9:00AM - 4:00PM","sunday":"Closed","tuesday":"9:00AM - 4:00PM","friday":"9:00AM - 4:00PM","saturday":"9:00AM - 4:00PM"},"name":"Vancouver, Washington"},{"exceptions":[{"exceptionHours":{"wednesday":"Closed","monday":"Closed","thursday":"Closed","sunday":"Closed","tuesday":"Closed","friday":"Closed","saturday":"Closed"},"startDate":"2025-12-03","name":"Annual Winter Closure","endDate":"2026-02-16"}],"description":"The Oregon City, Oregon facilities are open Fridays and Saturdays from 10:00 a.m. to 4:00 p.m. With the exception of its winter seasonal closure.","standardHours":{"wednesday":"Closed","monday":"Closed","thursday":"Closed","sunday":"Closed","tuesday":"Closed","friday":"10:00AM - 4:00PM","saturday":"10:00AM - 4:00PM"},"name":"Oregon City, Oregon"}]
        ,"addresses":[{"postalCode":"98661","city":"Vancouver","stateCode":"WA","countryCode":"US","provinceTerritoryCode":"","line1":"1501 E Evergreen Blvd.","type":"Physical","line3":"","line2":""},{"postalCode":"98661","city":"Vancouver","stateCode":"WA","countryCode":"US","provinceTerritoryCode":"","line1":"800 Hatheway Road, Bldg 722","type":"Mailing","line3":"","line2":""}]
        ,"images":[{"credit":"NPS Photo / Troy Wayrynen","title":"Fort Vancouver Garden","altText":"Fort Vancouver Garden","caption":"The garden at the reconstructed Fort Vancouver showcases the many plants that were grown at the historic Fort Vancouver.","url":"https://www.nps.gov/common/uploads/structured_data/3C7E8577-1DD8-B71B-0B5ABB3F175DDD81.jpg"},{"credit":"NPS Photo / Troy Wayrynen","title":"Black Powder Demonstrations at Fort Vancouver NHS","altText":"Black Powder Demonstrations at Fort Vancouver NHS","caption":"The military history at Vancouver Barracks is told through living history demonstrations, including historic weapons demonstrations.","url":"https://www.nps.gov/common/uploads/structured_data/3C7E86BF-1DD8-B71B-0B384E77223186E6.jpg"},{"credit":"NPS Photo / Troy Wayrynen","title":"Fort Vancouver","altText":"Fort Vancouver","caption":"A walk through the reconstructed Fort Vancouver is a highlight of any trip to Fort Vancouver National Historic Site.","url":"https://www.nps.gov/common/uploads/structured_data/3C7E881D-1DD8-B71B-0B98C9CFAE864249.jpg"},{"credit":"NPS Photo / Troy Wayrynen","title":"McLoughlin House","altText":"McLoughlin House","caption":"The McLoughlin House in Oregon City is a unit of Fort Vancouver National Historic Site. Here, visitors learn about Dr. John McLoughlin and the early history of Oregon state.","url":"https://www.nps.gov/common/uploads/structured_data/3C7E89A7-1DD8-B71B-0BCC6874B5F69230.jpg"},{"credit":"NPS Photo / Troy Wayrynen","title":"Pearson Air Museum","altText":"Pearson Air Museum","caption":"At Pearson Air Museum, visitors learn about the history of Pearson Field and early aviation in the Pacific Northwest.","url":"https://www.nps.gov/common/uploads/structured_data/3C7E8B4B-1DD8-B71B-0BE1CD3DD023CFEC.jpg"}]
        ,"weatherInfo":"Fort Vancouver National Historic Site is located in a mild, temperate climate. However, in the case of severe weather, park alerts posted on the park's website will provide information about emergency closures."

        ,"name":"Fort Vancouver"

        ,"designation":"National Historic Site"

        ,"multimedia":[]
        ,"relevanceScore":1.0

        }
        ,
        {

        "id":"07D9E506-B86A-4762-801F-2D333F369D5D"

        ,"url":

        "https://www.nps.gov/iafl/index.htm"

        ,"fullName":"Ice Age Floods National Geologic Trail"      

        ,"parkCode":"iafl"

        ,"description":"At the end of the last Ice Age, 18,000 to 15,000 years ago, an ice dam in northern Idaho created Glacial Lake Missoula stretching 3,000 square miles around Missoula, Montana. The dam burst and released flood waters across Washington, down the Columbia River into Oregon before reaching the Pacific Ocean. The Ice Age Floods forever changed the lives and landscape of the Pacific Northwest."

        ,"latitude":

        "46.5669309008" 

        ,"longitude":

        "-118.992564971" 

        ,"latLong":"lat:46.5669309008, long:-118.992564971"

        ,"activities":[{"id":"B33DC9B6-0B7D-4322-BAD7-A13A34C584A3","name":"Guided Tours"},{"id":"A0631906-9672-4583-91DE-113B93DB6B6E","name":"Self-Guided Tours - Walking"},{"id":"C7D5A145-F8EB-4C37-9E92-2F6C6206B196","name":"Self-Guided Tours - Auto"},{"id":"DF4A35E0-7983-4A3E-BC47-F37B872B0F25","name":"Junior Ranger Program"}]
        ,"topics":[{"id":"A160B3D9-1603-4D89-B82F-21FCAF9EEE3B","name":"Tragic Events"},{"id":"D91B6F1A-FED8-490D-B181-9940474CD67C","name":"Floods"},{"id":"F0F97E32-2F29-41B4-AF98-9FBE8DAB36B1","name":"Geology"}]
        ,"states":"WA,OR,ID,MT"

        ,"contacts":{"phoneNumbers":[{"phoneNumber":"(509) 237-9722","description":"","extension":"","type":"Voice"}],"emailAddresses":[{"description":"","emailAddress":"iafl_program_manager@nps.gov"}]}
        ,"entranceFees":[]
        ,"entrancePasses":[]
        ,"fees":[]
        ,"directionsInfo":"To Trail Headquarters from Spokane Take I-90 West At Exit 277, merge onto US-2 toward Davenport/Fairchild AFB/Spokane Airport Just past Wilbur, turn right onto WA-21 Stay left onto Highway 174 Turn right onto WA-155 Stay on WA-155 past Grand Coulee Dam and into the town of Coulee Dam Cross the Columbia River and continue on WA-155 Turn right onto Crest Drive and follow up the hill. The HQ Office is located on the left"

        ,"directionsUrl":"https://www.nps.gov/iafl/planyourvisit/directions.htm"

        ,"operatingHours":[{"exceptions":[],"description":"The Ice Age Floods National Geologic Trail is a collection of sites operated and owned by a variety of partners. , Hours of operation and season will vary from site to site. Please check each respective site for more details and information.","standardHours":{"wednesday":"All Day","monday":"All Day","thursday":"All Day","sunday":"All Day","tuesday":"All Day","friday":"All Day","saturday":"All Day"},"name":"Ice Age Floods National Geologic Trail"}]
        ,"addresses":[{"postalCode":"99116","city":"Coulee Dam","stateCode":"WA","countryCode":"US","provinceTerritoryCode":"","line1":"Program Manager","type":"Physical","line3":"1008 Crest Drive","line2":"Ice Age Floods National Geologic Trail"},{"postalCode":"99116","city":"Coulee Dam","stateCode":"WA","countryCode":"US","provinceTerritoryCode":"","line1":"Program Manager","type":"Mailing","line3":"1008 Crest Drive","line2":"Ice Age Floods National Geologic Trail"}]
        ,"images":[{"credit":"NPS Photo","title":"Green Monarch Ridge","altText":"Green Monarch Ridge by Lake Pend Oreille ID","caption":"Green Monarch Ridge by Lake Pend Oreille ID","url":"https://www.nps.gov/common/uploads/structured_data/A003C5CE-E0EB-2AC6-FC8EDC134B1DFD19.jpg"},{"credit":"NPS","title":"Steamboat Rock","altText":"Large butte with lake in the foreground","caption":"Steamboat Rock State Park, Washington","url":"https://www.nps.gov/common/uploads/structured_data/0E7BEDF2-952B-D9E0-038D808912A45A65.jpeg"},{"credit":"NPS","title":"Banks Lake","altText":"Steamboat rock in the foreground with Banks Lake in the distance","caption":"Banks Lake view from Steamboat Rock","url":"https://www.nps.gov/common/uploads/structured_data/D4C33AFB-B84F-E87C-E19705F8380417D1.jpeg"},{"credit":"NPS Photo","title":"Dry Lakes","altText":"Dry Lake lake bed and falls blanked with snow.","caption":"Dry Lake lake bed and falls in winter","url":"https://www.nps.gov/common/uploads/structured_data/693AFB1E-1DD8-B71B-0B7DAE958A82C347.jpg"},{"credit":"NPS/Cline","title":"Dry Falls","altText":"A overhead shot of the Dry Falls area","caption":"A beautiful shot from the ledge near the visitor's center in the Dry Falls area.","url":"https://www.nps.gov/common/uploads/structured_data/0BDAB952-1DD8-B71B-0B509D5898489BD4.jpg"}]
        ,"weatherInfo":"The National Geologic Trail is a collection of sites stretched across a four state area, the weather will vary from site to site. Please check the weather for your local region, or the region you'll be visiting, through their respective sites."

        ,"name":"Ice Age Floods"

        ,"designation":"National Geologic Trail"

        ,"multimedia":[]
        ,"relevanceScore":1.0

        }
        ]
        }
        """;
        List<Park> actual = null;
        try {
            actual = ParksModel.deserializeResponse(json);
        } catch (Exception e) {
            System.out.println(e);
        }
        assertEquals(actual.get(0).images().size(), 5);
        assertEquals(actual.get(2).name(), "Ice Age Floods");
        assertEquals(actual.get(0).images().get(0).credit(), "NPS Photo / H. Richards");

        Park expected = new Park("Fort Vancouver", "OR,WA", 
    "Located on the north bank of the Columbia River, in sight of snowy mountain peaks and a vibrant urban landscape, this park has a rich cultural past. From a frontier fur trading post, to a powerful military legacy, the magic of flight, and the origin of the American Pacific Northwest, history is shared at four unique sites. Discover stories of transition, settlement, conflict, and community.", 
            List.of(
                new Activity("09DF0950-D319-4557-A57E-04CD2F63FF42","Arts and Culture"),
                new Activity("C59E231D-55FC-4B37-BC5B-FF76383951B5", "Craft Demonstrations"),
                new Activity("FAED7F97-3474-4C21-AB42-FB0768A2F826", "Cultural Demonstrations"),
                new Activity("7CE6E935-F839-4FEC-A63E-052B1DEF39D2", "Biking"),
                new Activity("B33DC9B6-0B7D-4322-BAD7-A13A34C584A3", "Guided Tours"),
                new Activity("B204DE60-5A24-43DD-8902-C81625A09A74", "Living History"),
                new Activity("A8650547-1A30-4222-86C3-A7660A829670", "Reenactments"),
                new Activity("237A1662-6018-4115-ABD1-B8CCF827E703", "Historic Weapons Demonstration"),
                new Activity("DF4A35E0-7983-4A3E-BC47-F37B872B0F25", "Junior Ranger Program"),
                new Activity("7779241F-A70B-49BC-86F0-829AE332C708", "Playground"),
                new Activity("0B685688-3405-4E2A-ABBA-E3069492EC50", "Wildlife Watching"),
                new Activity("0C0D142F-06B5-4BE1-8B44-491B90F93DEB", "Park Film"),
                new Activity("C8F98B28-3C10-41AE-AA99-092B3B398C43", "Museum Exhibits"),
                new Activity("24380E3F-AD9D-4E38-BF13-C8EEB21893E7", "Shopping"),
                new Activity("467DC8B8-0B7D-436D-A026-80A22358F615", "Bookstore and Park Store")
            ),
            List.of(
                new Address("98661", "Vancouver", "WA", "1501 E Evergreen Blvd."),
                new Address("98661", "Vancouver", "WA", "800 Hatheway Road, Bldg 722")
            ), 
            List.of(
                new ParkImage(
                    "Fort Vancouver Garden", 
                    "https://www.nps.gov/common/uploads/structured_data/3C7E8577-1DD8-B71B-0B5ABB3F175DDD81.jpg", 
                    "NPS Photo / Troy Wayrynen"
                ),
                new ParkImage(
                    "Black Powder Demonstrations at Fort Vancouver NHS", 
                    "https://www.nps.gov/common/uploads/structured_data/3C7E86BF-1DD8-B71B-0B384E77223186E6.jpg", 
                    "NPS Photo / Troy Wayrynen"
                ),
                new ParkImage(
                    "Fort Vancouver", 
                    "https://www.nps.gov/common/uploads/structured_data/3C7E881D-1DD8-B71B-0B98C9CFAE864249.jpg", 
                    "NPS Photo / Troy Wayrynen"
                ),
                new ParkImage(
                    "McLoughlin House", 
                    "https://www.nps.gov/common/uploads/structured_data/3C7E89A7-1DD8-B71B-0BCC6874B5F69230.jpg", 
                    "NPS Photo / Troy Wayrynen"
                ),
                new ParkImage(
                    "Pearson Air Museum", 
                    "https://www.nps.gov/common/uploads/structured_data/3C7E8B4B-1DD8-B71B-0BE1CD3DD023CFEC.jpg", 
                    "NPS Photo / Troy Wayrynen"
                )),
            "fova");
            assertEquals(expected, actual.get(1));

        }
    @Test
    public void testGetFilteredParks() {
        Park park1 = new Park(
            "Yellowstone", "WY", "America's first national park", 
            List.of(new Activity("84902834092", "Hiking"), new Activity("93280409", "Fishing")),
            List.of(new Address("82190", "Yellowstone National Park", "WY", "1 Park Road")), 
            List.of(new ParkImage("Old Faithful eruption", "www.yellowstone.org/oldfaithful", "John Smith")), 
            "YELL");

        Park park2 = new Park("Grand Canyon", "AZ", "One of the seven natural wonders of the world", 
            List.of(
                new Activity("123456789", "Rafting"), 
                new Activity("9083409", "Hiking"), 
                new Activity("192837465", "Waterfall Viewing"), 
                new Activity("987654321", "Camping")),
            List.of(new Address("86023", "Grand Canyon Village", "AZ", "1 Main Park Road")), 
            List.of(new ParkImage("Canyon sunrise", "www.grandcanyon.com/sunrise", "Mary Johnson")), 
            "GRCA");

        Park park3 = new Park("Yosemite", "CA", "Famous for its giant sequoias and El Capitan", List.of(
                new Activity("564738291", "Rock Climbing"), 
                new Activity("192837465", "Waterfall Viewing"), 
                new Activity("918273645", "Wildlife Photography")),
            List.of(new Address("95389", "Yosemite Valley", "CA", "9000 Yosemite Park Drive")), 
            List.of(new ParkImage("Half Dome at dusk", "www.yosemite.org/halfdome", "Ansel Adams")), 
            "YOSE");

        Park park4 = new Park("Great Smoky Mountains", "TN", "Most visited national park in the US", 
            List.of(new Activity("756483920", "Autumn Leaf Viewing"), new Activity("384756291", "Historic Cabin Tours")),
            List.of(new Address("37738", "Gatlinburg", "TN", "107 Park Headquarters Road")), 
            List.of(new ParkImage("Misty mountain range", "www.greatsmokymountains.com/misty", "Sarah Wilson")), "GRSM");

        Park park5 = new Park("Zion", "UT", "Known for its red cliffs and narrow canyons", 
            List.of(
                new Activity("657483921", "Canyoneering"), 
                new Activity("192837465", "Waterfall Viewing"), 
                new Activity("192847563", "Scenic Driving"), 
                new Activity("564738291", "Stargazing")
            ),
            List.of(new Address("84767", "Springdale", "UT", "1 Zion Park Boulevard")), 
            List.of(new ParkImage("Angels Landing trail", "www.zionpark.org/angelslanding", "Mike Thompson")), "ZION");        

        List<Park> original = List.of(park1, park2, park3, park4, park5);
        model.setParkList(original);

        List<Park> actual = model.getFilteredParks(List.of("Waterfall Viewing"));
        List<Park> expected = List.of(park2, park3, park5);
        assertEquals(expected, actual);

        actual = model.getFilteredParks(List.of("Hiking"));
        expected = List.of(park1, park2);

        actual = model.getFilteredParks(List.of("Ice Skating"));
        expected = List.of();

        actual = model.getFilteredParks(List.of("Camping", "Scenic Driving"));
        expected = List.of(park2, park5);
        }

    // @Test
    // public void testGetParksByActivityName() {
    //     model.updateDB(List.of(park1, park2));

    //     List<Park> hikingParks = model.getParksByActivityName("Hiking");
    //     assertEquals(2, hikingParks.size());
    //     assertTrue(hikingParks.contains(park1));
    //     assertTrue(hikingParks.contains(park2));

    //     List<Park> fishingParks = model.getParksByActivityName("Fishing");
    //     assertEquals(1, fishingParks.size());
    //     assertTrue(fishingParks.contains(park1));

    //     List<Park> campingParks = model.getParksByActivityName("Camping");
    //     assertEquals(1, campingParks.size());
    //     assertTrue(campingParks.contains(park2));
    // }

    // @Test
    // public void testGetParkByParkCode() {
    //     model.updateDB(List.of(park1, park2));

    //     assertEquals(park1, model.getParkByParkCode("yell"));
    //     assertEquals(park1, model.getParkByParkCode("YELL"));
    //     assertEquals(park2, model.getParkByParkCode("gRcA"));
    // }
}