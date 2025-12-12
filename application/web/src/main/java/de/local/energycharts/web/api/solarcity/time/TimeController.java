package de.local.energycharts.web.api.solarcity.time;

import de.local.energycharts.core.solarcity.model.Time;
import de.local.energycharts.web.api.solarcity.time.model.FreezeNowRequest;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/test/time")
@Profile("test")
public class TimeController {

  @PutMapping(value = "/freeze-now", produces = "application/json")
  public void freezeNowAt(@RequestBody FreezeNowRequest request) {
    Time.freezeNowAt(request.getNow());
  }
}
