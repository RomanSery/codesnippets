package org.coderdreams.enums;

import java.util.List;
import java.util.stream.Collectors;

public enum State {
	ALABAMA("Alabama", "AL", CountryCode.US),
	ALASKA("Alaska", "AK", CountryCode.US),
	ARIZONA("Arizona", "AZ", CountryCode.US),
	ARKANSAS("Arkansas", "AR", CountryCode.US),
	CALIFORNIA("California", "CA", CountryCode.US),
	COLORADO("Colorado", "CO", CountryCode.US),
	CONNECTICUT("Connecticut", "CT", CountryCode.US),
	DELAWARE("Delaware", "DE", CountryCode.US),
	DISTRICT_OF_COLUMBIA("District of Columbia", "DC", CountryCode.US),
	FLORIDA("Florida", "FL", CountryCode.US),
	GEORGIA("Georgia", "GA", CountryCode.US),
	HAWAII("Hawaii", "HI", CountryCode.US),
	IDAHO("Idaho", "ID", CountryCode.US),
	ILLINOIS("Illinois", "IL", CountryCode.US),
	INDIANA("Indiana", "IN", CountryCode.US),
	IOWA("Iowa", "IA", CountryCode.US),
	KANSAS("Kansas", "KS", CountryCode.US),
	KENTUCKY("Kentucky", "KY", CountryCode.US),
	LOUISIANA("Louisiana", "LA", CountryCode.US),
	MAINE("Maine", "ME", CountryCode.US),
	MARYLAND("Maryland", "MD", CountryCode.US),
	MASSACHUSETTS("Massachusetts", "MA", CountryCode.US),
	MICHIGAN("Michigan", "MI", CountryCode.US),
	MINNESOTA("Minnesota", "MN", CountryCode.US),
	MISSISSIPPI("Mississippi", "MS", CountryCode.US),
	MISSOURI("Missouri", "MO", CountryCode.US),
	MONTANA("Montana", "MT", CountryCode.US),
	NEBRASKA("Nebraska", "NE", CountryCode.US),
	NEVADA("Nevada", "NV", CountryCode.US),
	NEW_HAMPSHIRE("New Hampshire", "NH", CountryCode.US),
	NEW_JERSEY("New Jersey", "NJ", CountryCode.US),
	NEW_MEXICO("New Mexico", "NM", CountryCode.US),
	NEW_YORK("New York", "NY", CountryCode.US),
	NORTH_CAROLINA("North Carolina", "NC", CountryCode.US),
	NORTH_DAKOTA("North Dakota", "ND", CountryCode.US),
	OHIO("Ohio", "OH", CountryCode.US),
	OKLAHOMA("Oklahoma", "OK", CountryCode.US),
	OREGON("Oregon", "OR", CountryCode.US),
	PENNSYLVANIA("Pennsylvania", "PA", CountryCode.US),
	PUERTO_RICO("Puerto Rico", "PR", CountryCode.US),
	RHODE_ISLAND("Rhode Island", "RI", CountryCode.US),
	SOUTH_CAROLINA("South Carolina", "SC", CountryCode.US),
	SOUTH_DAKOTA("South Dakota", "SD", CountryCode.US),
	TENNESSEE("Tennessee", "TN", CountryCode.US),
	TEXAS("Texas", "TX", CountryCode.US),
	UTAH("Utah", "UT", CountryCode.US),
	VERMONT("Vermont", "VT", CountryCode.US),
	VIRGIN_ISLANDS("Virgin Islands", "VI", CountryCode.US),
	VIRGINIA("Virginia", "VA", CountryCode.US),
	WASHINGTON("Washington", "WA", CountryCode.US),
	WEST_VIRGINIA("West Virginia", "WV", CountryCode.US),
	WISCONSIN("Wisconsin", "WI", CountryCode.US),
	WYOMING("Wyoming", "WY", CountryCode.US),
	AMERICAN_SAMOA("American Samoa", "AS", CountryCode.US),
	FEDERATED_STATES_OF_MICRONESIA("Federated States of Micronesia", "FM", CountryCode.US),
	GUAM("Guam", "GU", CountryCode.US),
	MARSHALL_ISLANDS("Marshall Islands", "MH", CountryCode.US),
	NORTHERN_MARIANA_ISLANDS("Northern Mariana Islands", "MP", CountryCode.US),
	PALAU("Palau", "PW", CountryCode.US),
	
	//Provinces
	ALBERTA("Alberta", "AB", CountryCode.CA),
	BRITISH_COLUMBIA("British Columbia", "BC", CountryCode.CA),
	MANITOBA("Manitoba", "MB", CountryCode.CA),
	NEW_BRUNSWICK("New Brunswick", "NB", CountryCode.CA),
	NEWFOUNDLAND_AND_LABRADOR("Newfoundland and Labrador", "NL", CountryCode.CA),
	NOVA_SCOTIA("Nova Scotia", "NS", CountryCode.CA),
	ONTARIO("Ontario", "ON", CountryCode.CA),
	PRINCE_EDWARD_ISLAND("Prince Edward Island", "PE", CountryCode.CA),
	QUEBEC("Québec", "QC", CountryCode.CA),
	SASKATCHEWAN("Saskatchewan", "SK", CountryCode.CA),
	
	//Territories
	NORTHWEST_TERRITORIES("Northwest Territories", "NT", CountryCode.CA),
	NUNAVUT("Nunavut", "NU", CountryCode.CA),
	YUKON("Yukon", "YT", CountryCode.CA)
	
	;
	
	private final String name;
	private final String abbreviation;
	private final CountryCode countryCode;

    public static final List<State> VALUES = List.of(State.values());

	State(String name, String abbreviation, CountryCode countryCode) {
        this.name = name;
        this.abbreviation = abbreviation;
        this.countryCode = countryCode;
    }
    
    public String getName() { return name; }
    public String getAbbreviation() { return abbreviation; }

    public static List<State> getByCountry(CountryCode countryCode) {
		if(countryCode == null) {
			return VALUES;
		}
		return VALUES.stream().filter(s -> s.countryCode == countryCode).collect(Collectors.toList());
	}

}