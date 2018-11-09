package unemployment.stats.tcruff.com;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class RetrieveStatisticsController {
	
	private static final String template = "Thank you for your patronage, %s!";
    private final AtomicLong counter = new AtomicLong();
    
	 @RequestMapping(value="/retrieveStatistics", method = RequestMethod.POST)
	 @ResponseBody
	 public RetrieveStatistics retrieveStatistics(@RequestBody RetrieveStatistics retrieveStatistics) {
	        return new RetrieveStatistics(retrieveStatistics, HttpStatus.OK);
	    }

}
