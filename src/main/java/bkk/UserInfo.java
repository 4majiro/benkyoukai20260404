package bkk;

import java.util.List;

import lombok.Data;

@Data
public class UserInfo {
	
	private String name;
	private String gender;
	private List<Integer> fruit;
	private String birthday;

}
