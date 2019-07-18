package com.techelevator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;

import com.techelevator.database_link.Campground;
import com.techelevator.database_link.CampgroundDAO;
import com.techelevator.database_link.JDBCCampgroundDAO;
import com.techelevator.database_link.JDBCParkDAO;
import com.techelevator.database_link.JDBCReservationDAO;
import com.techelevator.database_link.JDBCSiteDAO;
import com.techelevator.database_link.Park;
import com.techelevator.database_link.ParkDAO;
import com.techelevator.database_link.Reservation;
import com.techelevator.database_link.ReservationDAO;
import com.techelevator.database_link.Site;
import com.techelevator.database_link.SiteDAO;
import com.techelevator.menu.Menu;

public class CampgroundCLI {

	private String newLine = System.getProperty("line.separator");
	private String invalidEntry = newLine + "Invalid selection - please, try again." + newLine;
	private String successfulQuit = newLine + "The program has quit. Have a nice day!";
	private String viewParksMsg = newLine + "Select a Park for Further Details" + newLine;
	private String noAvailability = newLine
			+ "No campsites are available for those dates, would you like to enter other dates (1) or return to campsite menu (2)?"
			+ newLine;
	private String reservationConfirmation = newLine + "The reservation has been made, and the confirmation id is ";
	private double cost;
	private Menu menu;
	private ParkDAO parkDAO;
	private CampgroundDAO campgroundDAO;
	private ReservationDAO reservationDAO;
	private SiteDAO siteDAO;
	private Map<String, Park> allParks;
	private Map<Integer, Campground> campgrounds;
	private LocalDate localArrivalDate;
	private LocalDate localDepartureDate;

	public CampgroundCLI(DataSource dataSource) {
		campgroundDAO = new JDBCCampgroundDAO(dataSource);
		parkDAO = new JDBCParkDAO(dataSource);
		reservationDAO = new JDBCReservationDAO(dataSource);
		siteDAO = new JDBCSiteDAO(dataSource);
		menu = new Menu(System.in, System.out);

		allParks = parkDAO.allParks();
	}

	public void run() {
		while (true) {
			try {
				mainMenuLogic(allParks);
			} catch (Exception e) {
				menu.output(invalidEntry);
				break;
			}
		}
	}

	private void mainMenuLogic(Map<String, Park> parks) {
		while (true) {
			try {
				String input = menu.getInputAndOutput(concatParkMsg(parks));

				if (parks.containsKey(input)) {
					Park selectedPark = parks.get(input);

					parkInfoMenuLogic(selectedPark);
					break;

				} else {
					String quit = menu.getInputAndOutput("Are you sure you want to quit? (Y/N)");
					if (quit.equalsIgnoreCase("y")) {
						menu.output(successfulQuit);
						System.exit(0);
					} else {
						continue;
					}
				}
			} catch (Exception e) {
				menu.output(invalidEntry);
				break;
			}
		}
	}

	private void parkInfoMenuLogic(Park selectedPark) {

		while (true) {
			try {
				String parkInfoInput = menu.getInputAndOutput(parkInfo(selectedPark));

				if (parkInfoInput.equals("1")) {
					campgroundMenuLogic(selectedPark);
					break;
				} else if (parkInfoInput.equals("2")) {
					reservationSearchLogic(selectedPark);
					break;
				} else if (parkInfoInput.equals("3")) {
					break;
				} else {
					menu.output(invalidEntry);
				}
			} catch (Exception e) {
				menu.output(invalidEntry);
				break;
			}
		}
	}

	private void campgroundMenuLogic(Park selectedPark) {

		while (true) {
			try {
				String campgroundInfoInput = menu.getInputAndOutput(campgroundInfo(selectedPark));

				if (campgroundInfoInput.equals("1")) {
					reservationSearchLogic(selectedPark);
					break;
				} else if (campgroundInfoInput.equals("2")) {
					break;
				} else {
					menu.output(invalidEntry);
				}
			} catch (Exception e) {
				menu.output(invalidEntry);
				break;
			}
		}
	}

	private void reservationSearchLogic(Park selectedPark) {

		while (true) {
			try {
				String reservationSearchInput = menu.getInputAndOutput(concatCampgroundInfo(selectedPark, "") + newLine
						+ newLine + "Which campground (enter 0 to cancel)?" + newLine);
				int selectedSite = Integer.parseInt(reservationSearchInput);

				if (selectedSite == 0) {
					break;
				}

				else {
					String arrivalDate = menu
							.getInputAndOutput(newLine + "What is the arrival date? (YYYY-MM-DD)" + newLine);

					String departureDate = menu
							.getInputAndOutput(newLine + "What is the departure date? (YYYY-MM-DD)" + newLine);

					localArrivalDate = stringToDate(arrivalDate);
					localDepartureDate = stringToDate(departureDate);

					// if (reservationDAO.check(localArrivalDate, localDepartureDate,
					// campgrounds.get(selectedSite))) {
					if (siteDAO.allAvailableSites(campgrounds.get(selectedSite), localArrivalDate, localDepartureDate)
							.size() > 0) {
						calculateCost(campgrounds.get(selectedSite), localArrivalDate, localDepartureDate);
						reservationSiteLogic(campgrounds.get(selectedSite), localArrivalDate, localDepartureDate);
						break;
					} else {

						String choice = menu.getInputAndOutput(noAvailability);

						if (choice.equals("1")) {
							continue;
						}
					}
				}
				break;
			} catch (Exception e) {
				menu.output(invalidEntry);
				e.printStackTrace();
				break;
			}
		}
	}

	private void reservationSiteLogic(Campground campground, LocalDate localArrivalDate, LocalDate localDepartureDate) {
		try {
			String siteSelection = menu.getInputAndOutput(
					siteInfo(siteDAO.allAvailableSites(campground, localArrivalDate, localDepartureDate)));

			long selectedSite = Long.parseLong(siteSelection);

			if (selectedSite == 0) {
			}

			else {
				String reservationName = menu
						.getInputAndOutput("What name should the reservation be made under?" + newLine);

				Reservation reservation = createReservation(siteSelection, reservationName, localArrivalDate,
						localDepartureDate);

				reservationDAO.reserve(reservation);

				menu.output(newLine + reservationConfirmation + reservationDAO.getId(reservation) + newLine);
			}
		} catch (Exception e) {
			menu.output(invalidEntry);
		}
	}

	private Reservation createReservation(String siteSelection, String reservationName, LocalDate arrivalDate,
			LocalDate departureDate) {
		Reservation reservation = new Reservation();
		reservation.setSiteId(Long.parseLong(siteSelection));
		reservation.setName(reservationName);
		reservation.setFromDate(arrivalDate);
		reservation.setToDate(departureDate);
		reservation.setCreateDate(LocalDate.now());

		return reservation;
	}

	private String siteInfo(List<Site> availableSites) {

		String availableSiteInfo = newLine + "Results Matching Your Search Criteria" + newLine
				+ String.format("%-10s %-10s %-10s %-10s %-10s %-10s", "Site No.", "Max Occup.", "Accessible?",
						"Max RV Length", "Utility", "Cost")
				+ newLine;

		for (Site site : availableSites) {
			availableSiteInfo = availableSiteInfo
					+ String.format("%-10s %-10s %-10s %-10s %-10s %-10s", site.getSiteNumber(), site.getMaxOccupancy(),
							site.getAccessibility(), site.getMaxRvLength(), site.utilitiesExist(), "$" + cost)
					+ newLine;
		}

		return availableSiteInfo + newLine + newLine + "Which site should be reserved (enter 0 to cancel)?" + newLine;
	}

	private LocalDate stringToDate(String input) {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		LocalDate localDate = LocalDate.parse(input, formatter);

		return localDate;
	}

	private String concatCampgroundInfo(Park selectedPark, String strStart) {
		String info = newLine + strStart + newLine + newLine
				+ String.format("%4s  %-35s %-10s %-10s %-10s", "", "Name", "Open", "Close", "Daily Fee") + newLine;
		int selectedId = (int) selectedPark.getId();

		campgrounds = campgroundDAO.campgroundsFromPark(selectedId);
		int i = 1;

		for (Campground campground : campgrounds.values()) {
			info += String.format("%4s  %-35s %-10s %-10s %-10s", "#" + i, campground.getName(),
					monthName(campground.getOpenFrom()), monthName(campground.getOpenTo()),
					"$" + campground.getDailyFee()) + newLine;
			i++;
		}

		return info;
	}

	private String campgroundInfo(Park selectedPark) {
		String parkName = selectedPark.getName() + " National Park Campgrounds";

		String campgroundMenu = concatCampgroundInfo(selectedPark, parkName) + newLine + newLine + "Select a Command"
				+ newLine + "  1) Search for Available Reservation" + newLine + "  2) Return to Previous Screen"
				+ newLine;

		return campgroundMenu;
	}

	private String parkInfo(Park selectedPark) {

		String establishedDate = selectedPark.getEstablishDate().getMonthValue() + "/"
				+ selectedPark.getEstablishDate().getDayOfMonth() + "/" + selectedPark.getEstablishDate().getYear();

		String parkInfo = newLine + selectedPark.getName() + " National Park" + newLine
				+ String.format("%-20s  %-20s", "Location:", selectedPark.getLocation()) + newLine
				+ String.format("%-20s  %-20s", "Established:", establishedDate) + newLine
				+ String.format("%-20s  %-20s", "Area:", selectedPark.getArea() + " sq km") + newLine
				+ String.format("%-20s  %-20s", "Annual Visitors:", selectedPark.getAnnualVisitorCount()) + newLine
				+ newLine + multiLineDescription(selectedPark.getDescription()) + newLine + newLine + "Select a Command"
				+ newLine + "  1) View Campgrounds" + newLine + "  2) Search for Reservation" + newLine
				+ "  3) Return to Previous Screen" + newLine;

		return parkInfo;
	}

	private String multiLineDescription(String description) {

		String wrappedDescription = "";
		String lineOfDescription = "";

		String[] descriptionByWord = description.split(" ");

		for (String word : descriptionByWord) {
			if (lineOfDescription.length() <= 80) {
				lineOfDescription += word + " ";
			} else {
				wrappedDescription += lineOfDescription + newLine;
				lineOfDescription = "";
			}

		}

		return wrappedDescription + lineOfDescription;
	}

	private String concatParkMsg(Map<String, Park> parks) {
		String concatParkMsg = viewParksMsg;

		for (String key : parks.keySet()) {
			concatParkMsg = concatParkMsg + "  " + key + ") " + parks.get(key).getName() + newLine;
		}

		concatParkMsg = concatParkMsg + "  Q) quit" + newLine;

		return concatParkMsg;
	}

	private void calculateCost(Campground campground, LocalDate localArrivalDate, LocalDate localDepartureDate) {
		cost = campground.getDailyFee() * ChronoUnit.DAYS.between(localArrivalDate, localDepartureDate);
	}

	private String monthName(String monthNum) {
		Map<String, String> months = new HashMap<String, String>();

		months.put("01", "January");
		months.put("02", "February");
		months.put("03", "March");
		months.put("04", "April");
		months.put("05", "May");
		months.put("06", "June");
		months.put("07", "July");
		months.put("08", "August");
		months.put("09", "September");
		months.put("10", "October");
		months.put("11", "November");
		months.put("12", "December");

		return months.get(monthNum);
	}

	public static void main(String[] args) {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/campground");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");

		CampgroundCLI application = new CampgroundCLI(dataSource);

		application.run();
	}
}
