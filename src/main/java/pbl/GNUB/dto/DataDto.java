package pbl.GNUB.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DataDto {
    private String userInput;
    private String gptResponse;
    private String address;
    private String category;
    private String mainMenu;
    private List<String> tags; 

}
