package bkk;

import java.util.List;

import lombok.Data;

@Data
public class UserInfo {

	private String name;
	private String gender;
	private String bloodType;
	private List<String> favoriteFood;
	private String height;
	private String weight;
	private String bmi;
	private String dateOfBirth;

	public boolean isAllEmptyOrBlank() {
		return (name == null || name.isBlank()) &&
				(gender == null || gender.isBlank()) &&
				(bloodType == null || bloodType.isBlank()) &&
				(favoriteFood == null || favoriteFood.isEmpty()) &&
				(height == null || height.isBlank() || height.equals("NaN")) &&
				(weight == null || weight.isBlank() || weight.equals("NaN")) &&
				(bmi == null || bmi.isBlank() || bmi.equals("NaN"))  &&
				(dateOfBirth == null || dateOfBirth.isBlank());
	}

	public boolean isOnlyNamePresent() {
		boolean isNamePresent = name != null && !name.isBlank();
		return isNamePresent &&
				(gender == null || gender.isBlank()) &&
				(bloodType == null || bloodType.isBlank()) &&
				(favoriteFood == null || favoriteFood.isEmpty()) &&
				(height == null || height.isBlank() || height.equals("NaN")) &&
				(weight == null || weight.isBlank() || weight.equals("NaN")) &&
				(bmi == null || bmi.isBlank() || bmi.equals("NaN"))  &&
				(dateOfBirth == null || dateOfBirth.isBlank());
	}

}
