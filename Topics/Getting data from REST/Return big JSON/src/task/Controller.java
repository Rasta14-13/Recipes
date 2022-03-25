package task;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class Controller {

    Map<String, List<Colors>> map = Map.of("colors", List.of(
            new Colors("black", "hue", "primary",
                    new Codes(new Integer[]{0, 0, 0, 1}, "#000")),
            new Colors("white", "value", "primary",
                    new Codes(new Integer[]{255, 255, 255, 1}, "#FFF"))));

    @GetMapping("/colors")
    public ResponseEntity<Map<String, List<Colors>>> getColors() {
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

}