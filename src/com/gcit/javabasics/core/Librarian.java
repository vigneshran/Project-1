package com.gcit.javabasics.core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Scanner;
import java.sql.Date;

public class Librarian {
	
	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// LIBRARIAN // LIBRARIAN // LIBRARIAN // LIBRARIAN // LIBRARIAN // LIBRARIAN // LIBRARIAN // LIBRARIAN // LIBRARIAN // LIBRARIAN // LIBRARIAN // LIBRARIAN // LIBRARIAN // LIBRARIAN //
	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static Connection conn = null;
	public static void MAIN() throws SQLException	{
		
		Scanner scan = new Scanner(System.in);
		System.out.println("Welcome to GCIT Library Management System!");
		System.out.println("Which of the catagory user are you?");
		System.out.println("1. Librarian");
		System.out.println("2. Borrower");
		System.out.println("3. Administrator");
		int num = scan.nextInt();
		while (num != 1 & num != 2 & num != 3)	{
			System.out.println("Invalid Input! Try again!");
			num = scan.nextInt();
		}
		switch (num)	{
			case 1: LIB1();
					break;
			case 2: BORR();
					break;
			case 3: ADMIN();
					break;
		}
	}
	
	public static void LIB1() throws SQLException	{
		System.out.println("Enter branch you manage!");
		String query = "";
		HashMap<Integer, String> branchList = new HashMap<>();
		HashMap<String, Integer> branchIds = new HashMap<>();
		int i =1;
		try {
			
			Statement stmt = conn.createStatement();
			query = "SELECT branchId, branchName FROM tbl_library_branch";
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next())	{
				String branchName = rs.getString("branchName");
				int branchId = rs.getInt("branchId");
				System.out.println(i+ ". " + branchName);
				branchList.put(i, branchName);
				branchIds.put(branchName, branchId);
				i++;
			}
			System.out.println(i + ". " + "Quit to previous");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Scanner scan = new Scanner(System.in);
		int next = scan.nextInt();
		while (next < 1 || next > i) {
			System.out.println("Invalid input! Try again!");
			next = scan.nextInt();
		}
		if (next == i)	{
			MAIN();
		} else {
			String branchNameSelected = branchList.get(next);
			int branchIdSelected = branchIds.get(branchNameSelected);
			LIB2(branchIdSelected);
			
		}
	}
	public static void LIB2(int num) throws SQLException	{
		String branchName = "";
		try {
			Statement stmt = conn.createStatement();
			String query = "SELECT branchName FROM tbl_library_branch WHERE branchID = "+num+"";
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next())	{
				branchName = rs.getString("branchName");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("You are about to make changes to " + branchName + " library branch.");
		System.out.println("1. Update details of the " + branchName + " library");
		System.out.println("2. Add copies of book to the " + branchName + " library");
		System.out.println("3. Quit to previous");
		
		Scanner next = new Scanner(System.in);
		int num1 = next.nextInt();
		while (num1 != 1 & num1 != 2 & num1 != 3)	{
			System.out.println("Invalid Input! Try again!");
			num1 = next.nextInt();
		}
		switch(num1)	{
			case 1: librarianOPT1(num);
					break;
			case 2: librarianOPT2(num);
					break;
			case 3: ;
					LIB1();
					break;
			
		}
		
	}
	
	public static void librarianOPT1(int num) throws SQLException	{
		String branchName = "";
		
		try {
			Statement stmt = conn.createStatement();
			String query = "SELECT branchName FROM tbl_library_branch WHERE branchId = "+num+"";
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next())	{
				branchName = rs.getString("branchName");
			} 
		}catch (SQLException e)	{
				e.printStackTrace();
		}
			System.out.println("You have chosen to update the " + branchName + " with branchId " + num + ". Enter quit at any point to go back to the previous menu!");
			System.out.println("Enter the new branch name or enter N/A for no change");
			Scanner scan = new Scanner(System.in);
			String branchNameNew = scan.nextLine();
			if (branchNameNew.equals("quit")) LIB2(num);
			else if (branchNameNew.equals("N/A")) {
				System.out.println("No change happened. Now, enter the new branch address or enter N/A for no change");
				scan = new Scanner(System.in);
				String input = scan.nextLine();
				if (input.equals("quit")) LIB2(num);
				else if (input.equals("N/A")) {System.out.println("No Change happened!"); LIB2(num);}
				else	{
					try {
						Statement stmt = conn.createStatement();
						String query = "UPDATE tbl_library_branch SET branchAddress = '"+input+"' WHERE branchId = "+num+"";
						int omg = stmt.executeUpdate(query);
						System.out.println("No of rows affected: " + omg);
						MAIN();
					} catch (SQLException e)	{
						e.printStackTrace();
					}
				}
			}
			else	{
				try {
					Statement stmt1 = conn.createStatement();
					boolean matches = false;
					String query1 = "SELECT branchName FROM tbl_library_branch";
					ResultSet rs = stmt1.executeQuery(query1);
					while(rs.next())	{
						if (branchNameNew.equals(rs.getString("branchName"))) matches = true;
					}
					if (matches == true) {
						System.out.println("Branch name already exists.");
						librarianOPT1(num);
					}
					Statement stmt = conn.createStatement();
					String query = "UPDATE tbl_library_branch SET branchName = '"+branchNameNew+"' WHERE branchId = "+num+"";
					stmt.executeUpdate(query);
				} catch (SQLException e)	{
					e.printStackTrace();
				}
			}
			System.out.println("Enter the new branch address or enter N/A for no change");
			scan = new Scanner(System.in);
			String input = scan.nextLine();
			if (input.equals("quit")) LIB2(num);
			else if (input.equals("N/A")) LIB2(num);
			else	{
				try {
					Statement stmt = conn.createStatement();
					String query = "UPDATE tbl_library_branch SET branchAddress = '"+input+"' WHERE branchId = "+num+"";
					int omg = stmt.executeUpdate(query);
					;
					System.out.println("No of rows affected: " + omg);
					MAIN();
				} catch (SQLException e)	{
					e.printStackTrace();
				}
			}
	}

	public static void librarianOPT2(int num) throws SQLException	{
		String branchName = "";
		HashMap<Integer, String> dict = new HashMap<>();
		Integer no = null;
		Integer number = null;
		Integer numberPick = null;
		int i = 1;
		System.out.println("Pick the books you want to add copies off to your branch! Enter quit to go back to previous menu!");
		
		try {
			Statement stmt = conn.createStatement();
			String query = "SELECT title FROM tbl_book AS book JOIN tbl_book_copies AS copies ON book.bookId = copies.bookId JOIN tbl_library_branch AS branch ON copies.branchId = branch.branchId WHERE branch.branchId = "+num+"";
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next())	{
				branchName = rs.getString("title");
				System.out.println(i + ". " + branchName);
				dict.put(i, branchName);
				i++;
				
			}
		}catch (SQLException e)	{
				e.printStackTrace();
		}
		
		if (i==1)	{
			System.out.println("There are no books in this library. Going back!");
			LIB2(num);
		}
		Scanner scan = new Scanner(System.in);
		String num2 = scan.nextLine();
		
		if (num2.equals("quit")) LIB2(num);
		else {
			numberPick = Integer.parseInt(num2);
		}
		while (numberPick < 1 || numberPick >= i) {
			System.out.println("Invalid input! Try again!");
			numberPick = scan.nextInt();
		}

		try {
			Statement stmt = conn.createStatement();
			String query = "SELECT noOfCopies FROM tbl_book_copies copy JOIN tbl_book book ON copy.bookId = book.bookId WHERE book.title = '"+dict.get(numberPick)+"' AND copy.branchId = "+num+"";
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next())	{
				 no = rs.getInt("noOfCopies");
			}
		}catch (SQLException e)	{
				e.printStackTrace();
		}
		System.out.println("Existing number of copies is " + no + ". Enter the new number of copies! Enter N/A to quit go back to the previous ment!");
		String num3 = scan.nextLine();
		if (num3.equals("quit") || num3.equals("N/A")) {System.out.println("No change happened!"); LIB2(num);}
		else {
			number = Integer.parseInt(num3);
		}
		
		try {
			Statement stmt = conn.createStatement();
			String query = "UPDATE tbl_book_copies copy JOIN tbl_book book ON copy.bookId = book.bookId SET noOfCopies = "+number+" WHERE book.title = '"+dict.get(numberPick)+"' AND copy.branchId = "+num+"";
			int omg = stmt.executeUpdate(query);
			System.out.println("No of rows affected:" + omg);
			;
			MAIN();
		} catch (SQLException e)	{
				e.printStackTrace();
		}	
	}
	
	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// BORROWER // BORROWER // BORROWER // BORROWER // BORROWER // BORROWER // BORROWER // BORROWER // BORROWER // BORROWER // BORROWER // BORROWER // BORROWER // BORROWER // BORROWER ////// 
	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static void BORR() throws SQLException {
		Long cardNo = null;
		Scanner scan = new Scanner(System.in);
		System.out.println("Enter you card number! Type quit to go back.");
		String cardNoString = scan.nextLine();
		if (cardNoString.equals("quit")) MAIN();
		else {
			cardNo = Long.parseLong(cardNoString);
		}
		boolean matches = false;
		
		try {
			
			Statement stmt = conn.createStatement();
			String query = "SELECT DISTINCT cardNo FROM tbl_borrower;";
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next())	{

				if (cardNo == rs.getLong("cardNo")) matches = true;
				
			}
			
			if (matches == false) {
				System.out.println("Card number doesn't match.");
				BORR();
			}
		
		BORR1(cardNo);
		} catch (SQLException e)	{
		e.printStackTrace();
		}
	}
	
	public static void BORR1(long cardNo) throws SQLException	{
		String name ="";
		try {
			Statement stmt = conn.createStatement();
			String query = "SELECT name FROM tbl_borrower WHERE cardNo = "+cardNo+"";
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next())	{
				name = rs.getString("name");
			}
		} catch (SQLException e)	{
			e.printStackTrace();
		}
		System.out.println("Welcome, " + name + ". What do you want to today?");
		System.out.println("1. Check out a book");
		System.out.println("2. Return a book");
		System.out.println("3. Quit to previous");
		
		Scanner next = new Scanner(System.in);
		int num1 = next.nextInt();
		while (num1 != 1 & num1 != 2 & num1 != 3)	{
			System.out.println("Invalid Input! Try again!");
			num1 = next.nextInt();
		}
		switch(num1)	{
			case 1: BORROPT1(cardNo);
					break;
			case 2: BORROPT2(cardNo);
					break;
			case 3: ;
					BORR();
					break;
		}
	}
	
	public static void BORROPT1(long cardNo) throws SQLException	{
		System.out.println("Pick the branch you want to check out from! Enter quit to go back to previous menu!");
		HashMap<Integer, String> dict = new HashMap<>(); 
		HashMap<String, Integer> dictId = new HashMap<>();
		Integer numBranch = null;
		Integer numPick = null;
		int i = 1;
		try {
			Statement stmt = conn.createStatement();
			String query = "SELECT DISTINCT branchName, branchId FROM tbl_library_branch;";
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next())	{
				String s = rs.getString("branchName");
				int branchID = rs.getInt("branchId");
				System.out.println(i + ". " + s);
				dict.put(i, s);
				dictId.put(s, branchID);
				i++;
			}
		}catch (SQLException e)	{
				e.printStackTrace();
		}
		Scanner scan = new Scanner(System.in);
		String numBranchString = scan.nextLine();
		
		if (numBranchString.equals("quit")) BORR1(cardNo);
		else {
			numBranch = Integer.parseInt(numBranchString);
		}
		while (numBranch < 1 || numBranch >= i) {
			System.out.println("Invalid input! Try again!");
			numBranch = scan.nextInt();
		}
		
		System.out.println("Pick the book you want to check out! Enter quit to go back to previous menu!");
		HashMap<Integer, String> dict1 = new HashMap<>();
		HashMap<String, Integer> dict2 = new HashMap<>();
		HashMap<String, Integer> dict3 = new HashMap<>();
		i = 1;
		try {
			Statement stmt = conn.createStatement();
			String query = "SELECT DISTINCT book.title, copy.noOfCopies, book.bookId FROM tbl_book book JOIN tbl_book_copies copy ON copy.bookId = book.bookId JOIN tbl_library_branch branch ON copy.branchId = branch.branchId WHERE branchName = '"+dict.get(numBranch)+"' AND copy.noOfCopies > 0;";
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next())	{
				String s = rs.getString("title");
				int copies = rs.getInt("copy.noOfCopies");
				int bookId = rs.getInt("bookId");
				dict2.put(s, copies);
				System.out.println(i + ". " + s);
				dict1.put(i, s);
				dict3.put(s, bookId);
				i++;
			}
		}catch (SQLException e)	{
				e.printStackTrace();
		}
		if (i == 1)	{
			System.out.println("There are no books in this library. Going back!");
			BORROPT1(cardNo);
		}
		String numPickString = scan.nextLine();
		if (numPickString.equals("quit")) BORROPT1(cardNo);
		else {
			numPick = Integer.parseInt(numPickString);
		}
		while (numPick < 1 || numPick >= i) {
			System.out.println("Invalid input! Try again!");
			numPick = scan.nextInt();
		}
		
		String bookName = dict1.get(numPick);
		int noOfCopies = dict2.get(bookName);
		int bookId = dict3.get(bookName);
		int branchId = dictId.get(dict.get(numBranch));
		String branchName = dict.get(numBranch);
		noOfCopies--;
		String dueDate = "";
		// Decrementign the no of copies by 1
		try {
			conn.setAutoCommit(false); 
			Statement stmt = conn.createStatement();
			String query = "UPDATE tbl_book_copies copy JOIN tbl_book book ON copy.bookId = book.bookId JOIN tbl_library_branch branch ON branch.branchId = copy.branchId SET noOfCopies = "+noOfCopies+" WHERE branch.branchName = '"+dict.get(numBranch)+"' AND book.title = '"+dict1.get(numPick)+"'";
			int update = stmt.executeUpdate(query);
			System.out.println("No of rows affected: " + update);
		// Updating the book loan table
			PreparedStatement stmt1 = conn.prepareStatement("INSERT INTO tbl_book_loans (bookId, branchId, cardNo, dateOut  , dueDate) VALUES (?,?,?,NOW(), DATE_ADD(CURDATE(), INTERVAL 7 DAY))");

			stmt1.setInt(1, bookId);
			stmt1.setInt(2, branchId);
			stmt1.setLong(3, cardNo);
			
			int i1 = stmt1.executeUpdate();
			System.out.println("Records Updated: " + i1);
			conn.commit();
			query = "SELECT dueDate FROM tbl_book_loans WHERE cardNo = "+cardNo+" AND bookId = "+bookId+" AND branchId = "+branchId+"";
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next())	{
				dueDate = rs.getString("dueDate");
			}
			System.out.println("You have checked out the book: " + bookName+ " from " + branchName + ". " );
			System.out.println("The due-date is set to: " + dueDate);
			MAIN();
		} catch (SQLException e)	{
			conn.rollback();
			e.printStackTrace();
		}
	}
	
	public static void BORROPT2(long cardNo) throws SQLException{
		System.out.println("Pick the branch you want to return book to! Enter quit to go back to previous menu!");
		HashMap<Integer, String> dict = new HashMap<>(); 
		HashMap<String, Integer> dictId = new HashMap<>();
		Integer numBranch = null;
		Integer numPick = null;
		int i = 1;
		try {
			Statement stmt = conn.createStatement();
			String query = "SELECT DISTINCT branchName, branchId FROM tbl_library_branch;";
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next())	{
				String s = rs.getString("branchName");
				int branchID = rs.getInt("branchId");
				System.out.println(i + ". " + s);
				dict.put(i, s);
				dictId.put(s, branchID);
				i++;
			}
		}catch (SQLException e)	{
				e.printStackTrace();
		}
		Scanner scan = new Scanner(System.in);
		String numBranchString = scan.nextLine();
		if (numBranchString.equals("quit")) BORR1(cardNo);
		else {
			numBranch = Integer.parseInt(numBranchString);
		}
		while (numBranch < 1 || numBranch >= i) {
			System.out.println("Invalid input! Try again!");
			numBranch = scan.nextInt();
		}
		
		String branchName = dict.get(numBranch);
		int branchId = dictId.get(branchName);
		
		System.out.println("Pick the book you want to return back to " + branchName + "! Enter quit to go back to previous menu!");
		i = 1;
		HashMap<Integer, String> dict1 = new HashMap<>();
		HashMap<String, Integer> dict3 = new HashMap<>();
		HashMap<Integer, Integer> copies = new HashMap<>();
		HashMap<Integer, String> dateOuts = new HashMap<>();
		try {
			Statement stmt = conn.createStatement();
			String query = "SELECT book.title, book.bookId, loans.dateOut FROM tbl_book book JOIN tbl_book_loans loans ON loans.bookId = book.bookId WHERE loans.dateIn IS NULL AND loans.branchId = "+branchId+" AND loans.cardNo = "+cardNo+";";
			
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next())	{
				String s = rs.getString("book.title");
				int bookId = rs.getInt("book.bookId");
				String date = rs.getString("loans.dateOut");
				System.out.println(i + ". " + s + " which was checked out on " + date);
				dict1.put(i, s);
				dict3.put(s, bookId);
				dateOuts.put(i, date);
				i++;
			}
			query = "SELECT noOfCopies, bookId FROM tbl_book_copies WHERE branchId = "+branchId+"";
			rs = stmt.executeQuery(query);
			while(rs.next())	{
				int copy = rs.getInt("noOfCopies");
				int Id = rs.getInt("bookId");
				copies.put(Id, copy);
			}
		}catch (SQLException e)	{
				e.printStackTrace();
		}
		if (i==1)	{
			System.out.println("No books exist that you need to return from " + branchName +". Returning back!");
			BORROPT2(cardNo);
		}
		String numPickString = scan.nextLine();
		if (numPickString.equals("quit")) 	{
			BORR1(cardNo);
		}
		else	{
			numPick = Integer.parseInt(numPickString);
		}
		while (numPick < 1 || numPick >= i) {
			System.out.println("Invalid input! Try again!");
			numPick = scan.nextInt();
		}
		int bookId = dict3.get(dict1.get(numPick));
		String dateOutSelected = dateOuts.get(numPick);
		if (!copies.containsKey(bookId))	{
			try {
				PreparedStatement stmt = conn.prepareStatement("INSERT INTO tbl_book_copies VALUES(?,?,?)");
				stmt.setInt(1, bookId);
				stmt.setInt(2, branchId);
				stmt.setInt(3, 0);
				stmt.executeUpdate();
				String query = "SELECT noOfCopies, bookId FROM tbl_book_copies WHERE branchId = "+branchId+"";
				ResultSet rs = stmt.executeQuery(query);
				copies.clear();
				while(rs.next())	{
					int copy = rs.getInt("noOfCopies");
					int Id = rs.getInt("bookId");
					copies.put(Id, copy);
				}
			} catch (SQLException e)	{
					e.printStackTrace();
			}
			
		}
		int bookCopy = copies.get(bookId);
		bookCopy++;
		try {
			conn.setAutoCommit(false);
			Statement stmt = conn.createStatement();
			String query = "UPDATE tbl_book_copies copy JOIN tbl_book book ON copy.bookId = book.bookId JOIN tbl_library_branch branch ON branch.branchId = copy.branchId SET noOfCopies = "+bookCopy+" WHERE branch.branchName = '"+dict.get(numBranch)+"' AND book.title = '"+dict1.get(numPick)+"'";
			int update = stmt.executeUpdate(query);
			System.out.println("No of rows affected: " + update);
		// Updating the book loan table
			PreparedStatement stmt1 = conn.prepareStatement("UPDATE tbl_book_loans SET dateIn = CURDATE() WHERE cardNo = "+cardNo+" AND branchId = "+branchId+" AND bookId = "+bookId+" AND dateIn IS NULL AND dateOut = '"+dateOutSelected+"'");
			int i1 = stmt1.executeUpdate();
			System.out.println("Records Updated: " + i1);
			conn.commit();
			;
			MAIN();
		} catch (SQLException e)	{
			conn.rollback();
			e.printStackTrace();
		}
	}
	
	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ADMINISTRATOR // ADMINISTRATOR // ADMINISTRATOR // ADMINISTRATOR // ADMINISTRATOR // ADMINISTRATOR // ADMINISTRATOR // ADMINISTRATOR // ADMINISTRATOR // ADMINISTRATOR // ADMINISTRATOR 
	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static void ADMIN()	{
		System.out.println("Pick a selection:");
		System.out.println("1. Add/Update/Delete Author");
		System.out.println("2. Add/Update/Delete Book");
		System.out.println("3. Add/Update/Delete Publishers");
		System.out.println("4. Add/Update/Delete Library Branches");
		System.out.println("5. Add/Update/Delete Borrowers");
		System.out.println("6. Over-ride a book-loan date");
		System.out.println("7. Quit to previous");
		
		Scanner scan = new Scanner(System.in);
		int num1 = scan.nextInt();
		while (num1 != 1 & num1 != 2 & num1 != 3 & num1 != 4 & num1 !=5 & num1 != 6 & num1 != 7)	{
			System.out.println("Invalid Input! Try again!");
			num1 = scan.nextInt();
		}
		
		switch (num1) {
			case 1: ADMINAuthor();
					break;
			case 2: ADMINBook();
					break;
			case 3: ADMINPublisher();
					break;
			case 4: ADMINBranch();
					break;
			case 5: ADMINBorrower();
					break;
			case 6: ADMINOverride();
					break;
			case 7: try {
				MAIN();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
					break;
		}
		
	}
	
	public static void ADMINAuthor()	{
		System.out.println("You are about to make changes to the author databse. What do you want to do?");
		System.out.println("1. Add a new author");
		System.out.println("2. Update an author name");
		System.out.println("3. Delete an author");
		System.out.println("4. Quit to previous menu");
		
		Scanner scan = new Scanner(System.in);
		int num1 = scan.nextInt();
		while (num1 != 1 & num1 != 2 & num1 != 3 & num1 != 4)	{
			System.out.println("Invalid Input! Try again!");
			num1 = scan.nextInt();
		}
		
		switch (num1)	{
			case 1: addAuthor();
					break;
			case 2: updateAuthor();
					break;
			case 3: deleteAuthor();
			case 4: ADMIN();
					break;
		}
	}
	
	public static void addAuthor()	{
		System.out.println("You are about to add an author! Enter quit at anytime to go back to previous menu. Type an author's name:");
		Scanner scan = new Scanner(System.in);
		String newAuthor = scan.nextLine();
		Integer authorId = null;
		if (newAuthor.equals("quit")) ADMIN();
		try {
			String name = "";
			boolean matches = false;
			Statement stmt = conn.createStatement();
			String query1 = "SELECT authorName FROM tbl_author";
			ResultSet rs = stmt.executeQuery(query1);
			while(rs.next())	{
				name = rs.getString("authorName");
				if (newAuthor.equals(name)) matches = true;
			}
			if (matches == true) {
				System.out.println("Author already exists.");
				addAuthor();
			}
			PreparedStatement stmt1 = conn.prepareStatement("INSERT INTO tbl_author (authorName) VALUE (?)");
			stmt1.setString(1, newAuthor);
			int i1 = stmt1.executeUpdate();
			System.out.println("Records Updated: " + i1);
			String query = "SELECT authorId FROM tbl_author WHERE authorName = '"+newAuthor+"'";
			rs = stmt1.executeQuery(query);
			while(rs.next())	{
				authorId = rs.getInt("authorId");
				furtherQuestions(newAuthor, authorId);
			}
			
			
		} catch (SQLException e)	{
			e.printStackTrace();
		}
	}
	
	public static void furtherQuestions(String newAuthor, int authorId)	{
		System.out.println("Further options:");
		System.out.println("1. Did he/she author existing list of books in database?");
		System.out.println("2. Did he/she author a book that is not in the databse?");
		System.out.println("3. Quit to previous menu");
		Scanner scan = new Scanner(System.in);
		int num1 = scan.nextInt();
		
		while (num1 != 1 & num1 != 2 & num1 != 3)	{
			System.out.println("Invalid Input! Try again!");
			num1 = scan.nextInt();
		}
		switch(num1)	{
			case 1: connectExistingBook(newAuthor, authorId);
					break;
			case 2: connectNewBook(newAuthor, authorId);
					break;
			case 3: ADMIN();
					break;
		}
	}
	
	public static void connectExistingBook(String newAuthor, int newAuthorId)	{
		System.out.println("You are about to connect the newly created author to existing book list. Here are the list of books--press an option. Enter quit to go back to previous menu!");
		int i = 1;
		Integer numberBook = null;
		HashMap<Integer, String> bookList = new HashMap<>();
		HashMap<String, Integer> bookIds = new HashMap<>();
		try {
			Statement stmt = conn.createStatement();
			String query = "SELECT DISTINCT title, bookId FROM tbl_book;";
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next())	{
				String bookName = rs.getString("title");
				int Id = rs.getInt("bookId");
				bookList.put(i, bookName);
				bookIds.put(bookName, Id);
				System.out.println(i + ". " + bookName);
				i++;
			}
		}catch (SQLException e)	{
				e.printStackTrace();
		}
		Scanner scan = new Scanner(System.in);
		String numberBookString = scan.nextLine();
		if (numberBookString.equals("quit"))	{
			ADMIN();
		}
		else	{
			numberBook = Integer.parseInt(numberBookString);
		}
		while (numberBook < 1 || numberBook >= i) {
			System.out.println("Invalid input! Try again!");
			numberBook = scan.nextInt();
		}
		String bookNamePicked = bookList.get(numberBook);
		try {
			PreparedStatement stmt = conn.prepareStatement("INSERT INTO tbl_book_authors VALUES (?,?)");
			stmt.setInt(1, bookIds.get(bookNamePicked));
			stmt.setInt(2, newAuthorId);
			stmt.executeUpdate();
			System.out.println("Operation successful. Returning back to admin menu");
			ADMIN();
		}catch (SQLException e)	{
			e.printStackTrace();
		}
	}
	
	public static void connectNewBook(String newAuthor, int newAuthorId)	{
		System.out.println("You are about to connect the newly created author to a new book in list. Enter the new book name connected to the author you just entered. Enter quit to go back to previous menu!");
		Scanner scan = new Scanner(System.in);
		String newBook = scan.nextLine();
		Integer bookId = null;
		if (newBook.equals("quit")) ADMINAuthor();
		try {
			Statement stmt1 = conn.createStatement();
			boolean matches = false;
			String query1 = "SELECT title FROM tbl_book";
			ResultSet rs = stmt1.executeQuery(query1);
			while(rs.next())	{
				if (newBook.equals(rs.getString("title"))) matches = true;
			}
			if (matches == true) {
				System.out.println("Book already exists.");
				connectNewBook(newAuthor, newAuthorId);
			}
			PreparedStatement stmt = conn.prepareStatement("INSERT INTO tbl_book (title) VALUES (?)");
			stmt.setString(1, newBook);
			stmt.executeUpdate();
			String query = "SELECT bookId FROM tbl_book WHERE title = '"+newBook+"'";
			rs = stmt.executeQuery(query);
			while (rs.next())	{
				bookId = rs.getInt("bookId");
			}
			stmt = conn.prepareStatement("INSERT INTO tbl_book_authors VALUES(?,?)");
			stmt.setInt(1, bookId);
			stmt.setInt(2, newAuthorId);
			stmt.executeUpdate();
			System.out.println("Operation successful! Returning back to admin menu!");
			ADMIN();
		}catch (SQLException e)	{
			e.printStackTrace();
		}
	}
	
	public static void updateAuthor()	{
		System.out.println("You are about to update an existing author's name! Pick an option in the menu below. Type quit to go back to previous menu!");
		HashMap<Integer, String> authorList = new HashMap<>();
		HashMap<String, Integer> authorId = new HashMap<>();
		Integer authorSelect = null;
		int i=1;
		try {
			Statement stmt = conn.createStatement();
			String query = "SELECT DISTINCT authorName, authorId FROM tbl_author;";
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next())	{
				String authorName = rs.getString("authorName");
				int Id = rs.getInt("authorId");
				authorList.put(i, authorName);
				authorId.put(authorName, Id);
				System.out.println(i + ". " + authorName);
				i++;
			}
		}catch (SQLException e)	{
				e.printStackTrace();
		}
		Scanner scan = new Scanner(System.in);
		String authorSelectString = scan.nextLine();
		if (authorSelectString.equals("quit")) ADMINAuthor();
		else authorSelect = Integer.parseInt(authorSelectString);
		while (authorSelect < 1 || authorSelect >= i) {
			System.out.println("Invalid input! Try again!");
			authorSelect = scan.nextInt();
		}
		String authorNameSelected = authorList.get(authorSelect);
		int authorIdSelected = authorId.get(authorNameSelected);
		
		System.out.println("You are about to update the author name: " + authorNameSelected + ". Enter the new name. Press quit to go back to the previous menu!");
		String newAuthorName = scan.nextLine();
		if (newAuthorName.equals("quit")) ADMINAuthor();
		try {
			String query = "UPDATE tbl_author SET authorName = '"+newAuthorName+"' WHERE authorId = "+authorIdSelected+"";
			Statement stmt = conn.createStatement();
			int number = stmt.executeUpdate(query);
			System.out.println("Records updated: " + number);
			System.out.println("Operation successful. Returning back.");
			ADMIN();
			
		}catch (SQLException e)	{
			e.printStackTrace();
		}
	}
	
	public static void deleteAuthor()	{
		System.out.println("You are about to delete an existing author's name! Pick an option in the menu below. Type quit to go back to previous menu!");
		HashMap<Integer, String> authorList = new HashMap<>();
		HashMap<String, Integer> authorId = new HashMap<>();
		Integer authorSelect = null;
		int i=1;
		try {
			Statement stmt = conn.createStatement();
			String query = "SELECT DISTINCT authorName, authorId FROM tbl_author;";
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next())	{
				String authorName = rs.getString("authorName");
				int Id = rs.getInt("authorId");
				authorList.put(i, authorName);
				authorId.put(authorName, Id);
				System.out.println(i + ". " + authorName);
				i++;
			}
		}catch (SQLException e)	{
				e.printStackTrace();
		}
		Scanner scan = new Scanner(System.in);
		String authorSelectString = scan.nextLine();
		if (authorSelectString.equals("quit")) ADMINAuthor();
		else authorSelect = Integer.parseInt(authorSelectString);
		String authorNameSelected = authorList.get(authorSelect);
		int authorIdSelected = authorId.get(authorNameSelected);
		
		System.out.println("You are about to delete the author name: " + authorNameSelected + ". Type yes to confirm operation. Press quit to go back to the previous menu!");
		String newAuthorName = scan.nextLine();
		if (newAuthorName.equals("quit")) ADMINAuthor();
		else if (newAuthorName.equals("yes"))	{
			try {
				String query = "DELETE FROM tbl_author WHERE authorId = '"+authorIdSelected+"'";
				Statement stmt = conn.createStatement();
				int number = stmt.executeUpdate(query);
				System.out.println("Records updated: " + number);
				System.out.println("Operation successful. Returning back.");
				ADMIN();
			
			}catch (SQLException e)	{
				e.printStackTrace();
			}
		}
		else deleteAuthor();
	}
	
	
	public static void ADMINBook()	{
		System.out.println("You are about to make changes to the book databse. What do you want to do?");
		System.out.println("1. Add a new book");
		System.out.println("2. Update an book name");
		System.out.println("3. Delete a book");
		System.out.println("4. Quit to previous menu");
		
		Scanner scan = new Scanner(System.in);
		int num1 = scan.nextInt();
		while (num1 != 1 & num1 != 2 & num1 != 3 & num1 != 4)	{
			System.out.println("Invalid Input! Try again!");
			num1 = scan.nextInt();
		}
		
		switch (num1)	{
			case 1: addBook();
					break;
			case 2: updateBook();
					break;
			case 3: deleteBook();
			case 4: ADMIN();
					break;
		}
	}
	
	public static void addBook()	{
		System.out.println("You are about to add a book! Enter quit at anytime to go back to previous menu. Type a book's name:");
		Scanner scan = new Scanner(System.in);
		String newBook = scan.nextLine();
		Integer bookId = null;
		if (newBook.equals("quit")) ADMINBook();
		try {
			Statement stmt = conn.createStatement();
			boolean matches = false;
			String query1 = "SELECT title FROM tbl_book";
			ResultSet rs = stmt.executeQuery(query1);
			while(rs.next())	{
				if (newBook.equals(rs.getString("title"))) matches = true;
			}
			if (matches == true) {
				System.out.println("Book already exists.");
				addBook();
			}
			PreparedStatement stmt1 = conn.prepareStatement("INSERT INTO tbl_book (title) VALUE (?)");
			stmt1.setString(1, newBook);
			int i1 = stmt1.executeUpdate();
			System.out.println("Records Updated: " + i1);
			String query = "SELECT bookId FROM tbl_book WHERE title = '"+newBook+"'";
			rs = stmt.executeQuery(query);
			while(rs.next())	{
				bookId = rs.getInt("bookId");
				furtherQuestionsBook(newBook, bookId);
			}
		} catch (SQLException e)	{
			e.printStackTrace();
		}
	}
	
	public static void furtherQuestionsBook(String newBook, int bookId)	{
		System.out.println("Further options:");
		System.out.println("1. Do you want the book connected to existing authors in the databse?");
		System.out.println("2. Do you want the book connected to new author that does not exist in the database?");
		System.out.println("3. Quit to previous menu");
		Scanner scan = new Scanner(System.in);
		int num1 = scan.nextInt();
		
		while (num1 != 1 & num1 != 2 & num1 != 3)	{
			System.out.println("Invalid Input! Try again!");
			num1 = scan.nextInt();
		}
		switch(num1)	{
			case 1: connectExistingAuthor(newBook, bookId);
					break;
			case 2: connectNewAuthor(newBook, bookId);
					break;
			case 3: ADMIN();
					break;
		}
	}
	
	public static void connectExistingAuthor(String newBook, int bookId)	{
		System.out.println("You are about to connect the newly created book to existing author list. Here are the list of books--press an option. Enter quit to go back to previous menu!");
		int i = 1;
		Integer numberAuthor = null;
		HashMap<Integer, String> authorList = new HashMap<>();
		HashMap<String, Integer> authorIds = new HashMap<>();
		try {
			Statement stmt = conn.createStatement();
			String query = "SELECT DISTINCT authorName, authorId FROM tbl_author;";
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next())	{
				String authorName = rs.getString("authorName");
				int Id = rs.getInt("authorId");
				authorList.put(i, authorName);
				authorIds.put(authorName, Id);
				System.out.println(i + ". " + authorName);
				i++;
			}
		}catch (SQLException e)	{
				e.printStackTrace();
		}
		Scanner scan = new Scanner(System.in);
		String numberAuthorString = scan.nextLine();
		if (numberAuthorString.equals("quit"))	{
			ADMIN();
		}
		else	{
			numberAuthor = Integer.parseInt(numberAuthorString);
		}
		while (numberAuthor < 1 || numberAuthor > i) {
			System.out.println("Invalid input! Try again!");
			numberAuthor = scan.nextInt();
		}
		String authorNamePicked = authorList.get(numberAuthor);
		try {
			PreparedStatement stmt = conn.prepareStatement("INSERT INTO tbl_book_authors VALUES (?,?)");
			stmt.setInt(1, bookId);
			stmt.setInt(2, authorIds.get(authorNamePicked));
			stmt.executeUpdate();
			System.out.println("Operation successful. Returning back to admin menu");
			ADMIN();
		}catch (SQLException e)	{
			e.printStackTrace();
		}
	}
	
	public static void connectNewAuthor(String newBook, int newBookId)	{
		System.out.println("You are about to connect the newly created book to a new author in list. Enter the new author name connected to the book you just entered. Enter quit to go back to previous menu!");
		Scanner scan = new Scanner(System.in);
		String newAuthorName = scan.nextLine();
		Integer authorId = null;
		if (newAuthorName.equals("quit")) ADMINAuthor();
		try {
			Statement stmt1 = conn.createStatement();
			boolean matches = false;
			String query1 = "SELECT authorName FROM tbl_author";
			ResultSet rs = stmt1.executeQuery(query1);
			while(rs.next())	{
				if (newAuthorName.equals(rs.getString("authorName"))) matches = true;
			}
			if (matches == true) {
				System.out.println("Author already exists.");
				connectNewAuthor(newBook, newBookId);
			}
			PreparedStatement stmt = conn.prepareStatement("INSERT INTO tbl_author (authorName) VALUES (?)");
			stmt.setString(1, newAuthorName);
			stmt.executeUpdate();
			String query = "SELECT authorId FROM tbl_author WHERE authorName = '"+newAuthorName+"'";
			rs = stmt.executeQuery(query);
			while (rs.next())	{
				authorId = rs.getInt("authorId");
			}
			stmt = conn.prepareStatement("INSERT INTO tbl_book_authors VALUES(?,?)");
			stmt.setInt(1, newBookId);
			stmt.setInt(2, authorId);
			stmt.executeUpdate();
			System.out.println("Operation successful! Returning back to admin menu!");
			ADMIN();
		}catch (SQLException e)	{
			e.printStackTrace();
		}
	}
	
	public static void updateBook()	{
		System.out.println("You are about to update an existing book's name! Pick an option in the menu below. Type quit to go back to previous menu!");
		HashMap<Integer, String> bookList = new HashMap<>();
		HashMap<String, Integer> bookId = new HashMap<>();
		Integer bookSelect = null;
		int i=1;
		try {
			Statement stmt = conn.createStatement();
			String query = "SELECT DISTINCT title, bookId FROM tbl_book;";
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next())	{
				String bookName = rs.getString("title");
				int Id = rs.getInt("bookId");
				bookList.put(i, bookName);
				bookId.put(bookName, Id);
				System.out.println(i + ". " + bookName);
				i++;
			}
		}catch (SQLException e)	{
				e.printStackTrace();
		}
		Scanner scan = new Scanner(System.in);
		String bookSelectString = scan.nextLine();
		if (bookSelectString.equals("quit")) ADMINAuthor();
		else bookSelect = Integer.parseInt(bookSelectString);
		while (bookSelect < 1 || bookSelect > i) {
			System.out.println("Invalid input! Try again!");
			bookSelect = scan.nextInt();
		}
		String bookNameSelected = bookList.get(bookSelect);
		int bookIdSelected = bookId.get(bookNameSelected);
		
		System.out.println("You are about to update the book name: " + bookNameSelected + ". Enter the new name. Press quit to go back to the previous menu!");
		String newBookName = scan.nextLine();
		if (newBookName.equals("quit")) ADMINAuthor();
		try {
			String query = "UPDATE tbl_book SET title = '"+newBookName+"' WHERE bookId = "+bookIdSelected+"";
			Statement stmt = conn.createStatement();
			int number = stmt.executeUpdate(query);
			System.out.println("Records updated: " + number);
			System.out.println("Operation successful. Returning back.");
			ADMIN();
			
		}catch (SQLException e)	{
			e.printStackTrace();
		}
	}
	
	public static void deleteBook()	{
		System.out.println("You are about to delete an existing book's name! Pick an option in the menu below. Type quit to go back to previous menu!");
		HashMap<Integer, String> bookList = new HashMap<>();
		HashMap<String, Integer> bookId = new HashMap<>();
		Integer bookSelect = null;
		int i=1;
		try {
			Statement stmt = conn.createStatement();
			String query = "SELECT DISTINCT title, bookId FROM tbl_book;";
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next())	{
				String bookName = rs.getString("title");
				int Id = rs.getInt("bookId");
				bookList.put(i, bookName);
				bookId.put(bookName, Id);
				System.out.println(i + ". " + bookName);
				i++;
			}
		}catch (SQLException e)	{
				e.printStackTrace();
		}
		Scanner scan = new Scanner(System.in);
		String bookSelectString = scan.nextLine();
		if (bookSelectString.equals("quit")) ADMINBook();
		else bookSelect = Integer.parseInt(bookSelectString);
		String bookNameSelected = bookList.get(bookSelect);
		int bookIdSelected = bookId.get(bookNameSelected);
		
		System.out.println("You are about to delete the book name: " + bookNameSelected + ". Type yes to confirm operation. Press quit to go back to the previous menu!");
		String newAuthorName = scan.nextLine();
		if (newAuthorName.equals("quit")) ADMINAuthor();
		else if (newAuthorName.equals("yes"))	{
			try {
				String query = "DELETE FROM tbl_book WHERE bookId = '" + bookIdSelected+"'";
				Statement stmt = conn.createStatement();
				int number = stmt.executeUpdate(query);
				System.out.println("Records updated: " + number);
				System.out.println("Operation successful. Returning back.");
				ADMIN();
			
			}catch (SQLException e)	{
				e.printStackTrace();
			}
		}
		else deleteBook();
	}
	
	public static void ADMINPublisher()	{
		System.out.println("You are about to make changes to the publisher daabase. Pick an option!");
		System.out.println("1. Add a new publisher");
		System.out.println("2. Update a publisher's name/address/phone");
		System.out.println("3. Delete a publisher");
		System.out.println("4. Quit to previous menu");
		
		Scanner scan = new Scanner(System.in);
		int num1 = scan.nextInt();
		while (num1 != 1 & num1 != 2 & num1 != 3 & num1 != 4)	{
			System.out.println("Invalid Input! Try again!");
			num1 = scan.nextInt();
		}
		
		switch (num1)	{
			case 1: addPublisher();
					break;
			case 2: updatePublisher();
					break;
			case 3: deletePublisher();
			case 4: ADMIN();
					break;
		}
	}
	
	public static void addPublisher()	{
		System.out.println("You are about to add a publisher to the database. Enter quit at any time to go back to the previous menu. Enter publisher's name!");
		Scanner scan = new Scanner(System.in);
		String newPublisherName = scan.nextLine();
		Integer pubId = null;
		if (newPublisherName.equals("quit")) ADMINPublisher();
		try {
			Statement stmt1 = conn.createStatement();
			boolean matches = false;
			String query1 = "SELECT publisherName FROM tbl_publisher";
			ResultSet rs = stmt1.executeQuery(query1);
			while(rs.next())	{
				if (newPublisherName.equals(rs.getString("publisherName"))) matches = true;
			}
			if (matches == true) {
				System.out.println("Publisher already exists.");
				addPublisher();
			}
			Statement stmt = conn.createStatement();
			String query = "INSERT INTO tbl_publisher (publisherName) VALUES ('"+newPublisherName+"')";
			int num = stmt.executeUpdate(query);
			System.out.println("Records updated: " + num);
			query = "SELECT publisherId FROM tbl_publisher WHERE publisherName = '"+newPublisherName+"'";
			 rs = stmt.executeQuery(query);
			while(rs.next())	{
				pubId = rs.getInt("publisherId");
			}
			System.out.println("Do you want to add more details about this publisher?");
			System.out.println("Type the publisher's phone number. Enter quit to go to next option");
			
			addPubPhone(pubId);
			System.out.println("Enter the publisher's address. Enter quit to go back to previous menu!");
			addPubAddress(pubId);
			int num1 = scan.nextInt();
			
			
			
		}catch (SQLException e)	{
				e.printStackTrace();
		}
	}
	
	public static void addPubAddress(int pubId)	{
		Scanner scan = new Scanner(System.in);
		String pubAdd = scan.nextLine();
		if (pubAdd.equals("quit")) ADMINPublisher();
		try	{
			Statement stmt = conn.createStatement();
			String query = "UPDATE tbl_publisher SET publisherAddress = '"+pubAdd+"' WHERE publisherId = "+pubId+"";
			int num = stmt.executeUpdate(query);
			System.out.println(num + " address added");	
			System.out.println("Operation successful. Going back.");
			ADMINPublisher();
		} catch (SQLException e)	{
			e.printStackTrace();
		}
	}
	public static void addPubPhone(int pubId)	{
		Scanner scan = new Scanner(System.in);
		String pubPhone = scan.nextLine();
		if (pubPhone.equals("quit")) {
			System.out.println("Enter the publisher's address. Enter quit to go back to previous menu!");
			addPubAddress(pubId);
		}
		try	{
			Statement stmt = conn.createStatement();
			String query = "UPDATE tbl_publisher SET publisherPhone = '"+pubPhone+"' WHERE publisherId = "+pubId+"";
			int num = stmt.executeUpdate(query);
			System.out.println(num + " phone added");	
		} catch (SQLException e)	{
			e.printStackTrace();
		}
	}
	
	public static void updatePublisher()	{
		System.out.println("You are about to update the details of an existing publiser. Enter quit at any point to skip the operation. Pick a publisher from the menu below:");
		HashMap<Integer, String> publisherList = new HashMap<>();
		HashMap<String, Integer> publisherId = new HashMap<>();
		Integer pubSelect = null;
		int i=1;
		try {
			Statement stmt = conn.createStatement();
			String query = "SELECT DISTINCT publisherName, publisherId FROM tbl_publisher;";
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next())	{
				String pubName = rs.getString("publisherName");
				int Id = rs.getInt("publisherId");
				publisherList.put(i, pubName);
				publisherId.put(pubName, Id);
				System.out.println(i + ". " + pubName);
				i++;
			}
		}catch (SQLException e)	{
				e.printStackTrace();
		}
		Scanner scan = new Scanner(System.in);
		String pubSelectString = scan.nextLine();
		if (pubSelectString.equals("quit")) ADMINPublisher();
		else pubSelect = Integer.parseInt(pubSelectString);
		while (pubSelect < 1 || pubSelect >= i) {
			System.out.println("Invalid input! Try again!");
			pubSelect = scan.nextInt();
		}
		String pubNameSelected = publisherList.get(pubSelect);
		int pubIdSelected = publisherId.get(pubNameSelected);
		
		System.out.println("Enter the new name of the publisher. Quit to go to next option");
		String newName = scan.nextLine();
		if (newName.equals("quit")) updatePubAddress(pubIdSelected);
		try	{
			Statement stmt = conn.createStatement();
			String query = "UPDATE tbl_publisher SET publisherName = '"+newName+"' WHERE publisherId = "+pubIdSelected+" ";
			int i1 = stmt.executeUpdate(query);
			System.out.println(i1 + " name(s) updated!");
			updatePubAddress(pubIdSelected);
		} catch (SQLException e)	{
			e.printStackTrace();
		}
	}
	public static void updatePubAddress(int pubId)	{
		System.out.println("Enter the new address of the selected publisher. Quit to go to next option");
		Scanner scan = new Scanner(System.in);
		String pubAdd = scan.nextLine();
		if (pubAdd.equals("quit")) updatePubPhone(pubId);
		try	{
			Statement stmt = conn.createStatement();
			String query = "UPDATE tbl_publisher SET publisherAddress = '"+pubAdd+"' WHERE publisherId = "+pubId+"";
			int num = stmt.executeUpdate(query);
			System.out.println(num + " address added");	
			System.out.println("Operation successful. Going back.");
			updatePubPhone(pubId);
		} catch (SQLException e)	{
			e.printStackTrace();
		}
	}
	public static void updatePubPhone(int pubId)	{
		System.out.println("Enter the new phone number of the selected publisher. Quit to go to go back ");
		Scanner scan = new Scanner(System.in);
		String pubPhone = scan.nextLine();
		if (pubPhone.equals("quit")) ADMINPublisher();
		try	{
			Statement stmt = conn.createStatement();
			String query = "UPDATE tbl_publisher SET publisherPhone = '"+pubPhone+"' WHERE publisherId = "+pubId+"";
			int num = stmt.executeUpdate(query);
			System.out.println(num + " phone added");	
			ADMINPublisher();
		} catch (SQLException e)	{
			e.printStackTrace();
		}
	}
	
	public static void deletePublisher()	{
		System.out.println("You are about to delete an existing publisher's name! Pick an option in the menu below. Type quit to go back to previous menu!");
		HashMap<Integer, String> publisherList = new HashMap<>();
		HashMap<String, Integer> publisherId = new HashMap<>();
		Integer pubSelect = null;
		int i=1;
		try {
			Statement stmt = conn.createStatement();
			String query = "SELECT DISTINCT publisherName, publisherId FROM tbl_publisher;";
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next())	{
				String pubName = rs.getString("publisherName");
				int Id = rs.getInt("publisherId");
				publisherList.put(i, pubName);
				publisherId.put(pubName, Id);
				System.out.println(i + ". " + pubName);
				i++;
			}
		}catch (SQLException e)	{
				e.printStackTrace();
		}
		Scanner scan = new Scanner(System.in);
		String pubSelectString = scan.nextLine();
		if (pubSelectString.equals("quit")) ADMINPublisher();
		else pubSelect = Integer.parseInt(pubSelectString);
		while (pubSelect < 1 || pubSelect >= i) {
			System.out.println("Invalid input! Try again!");
			pubSelect = scan.nextInt();
		}
		String pubNameSelected = publisherList.get(pubSelect);
		int pubIdSelected = publisherId.get(pubNameSelected);
		
		System.out.println("You are about to delete the author name: " + pubNameSelected + ". Type yes to confirm operation. Press quit to go back to the previous menu!");
		String newPubName = scan.nextLine();
		if (newPubName.equals("quit")) ADMINPublisher();
		else if (newPubName.equals("yes"))	{
			try {
				String query = "DELETE FROM tbl_publisher WHERE publisherId = '" + pubIdSelected+"'";
				Statement stmt = conn.createStatement();
				int number = stmt.executeUpdate(query);
				System.out.println("Records updated: " + number);
				System.out.println("Operation successful. Returning back.");
				ADMINPublisher();
			}catch (SQLException e)	{
				e.printStackTrace();
			}
		}
		else {
			System.out.println("Invalid input. Try again!");
			deletePublisher();
		}
	}
	
	public static void ADMINBranch()	{
		System.out.println("You are about to make changes to the library branch database. Pick an option!");
		System.out.println("1. Add a new branch");
		System.out.println("2. Update a branch's name/address");
		System.out.println("3. Delete a branch");
		System.out.println("4. Quit to previous menu");
		
		Scanner scan = new Scanner(System.in);
		int num1 = scan.nextInt();
		while (num1 != 1 & num1 != 2 & num1 != 3 & num1 != 4)	{
			System.out.println("Invalid Input! Try again!");
			num1 = scan.nextInt();
		}
		
		switch (num1)	{
			case 1: addBranch();
					break;
			case 2: updateBranch();
					break;
			case 3: deleteBranch();
			case 4: ADMIN();
					break;
		}
	}
	
	public static void addBranch()	{
		System.out.println("You are about to add a branch to the database. Enter quit at any time to go back to the previous menu. Enter branch's name!");
		Scanner scan = new Scanner(System.in);
		String newBranchName = scan.nextLine();
		Integer branchId = null;
		if (newBranchName.equals("quit")) ADMINBranch();
		try {
			Statement stmt1 = conn.createStatement();
			boolean matches = false;
			String query1 = "SELECT branchName FROM tbl_library_branch";
			ResultSet rs = stmt1.executeQuery(query1);
			while(rs.next())	{
				if (newBranchName.equals(rs.getString("branchName"))) matches = true;
			}
			if (matches == true) {
				System.out.println("Branch already exists.");
				addBranch();
			}
			Statement stmt = conn.createStatement();
			String query = "INSERT INTO tbl_library_branch (branchName) VALUES ('"+newBranchName+"')";
			int num = stmt.executeUpdate(query);
			System.out.println("Records updated: " + num);
			query = "SELECT branchId FROM tbl_library_branch WHERE branchName = '"+newBranchName+"'";
			 rs = stmt.executeQuery(query);
			while(rs.next())	{
				branchId = rs.getInt("branchId");
			}
			System.out.println("Do you want to add more details about this branch?");
			System.out.println("Type the branch's address. Enter quit to go to previous menu");
			addBranchAdd(branchId);
		}catch (SQLException e)	{
				e.printStackTrace();
		}
	}
	
	public static void addBranchAdd(int branchId)	{
		Scanner scan = new Scanner(System.in);
		String branchAdd = scan.nextLine();
		if (branchAdd.equals("quit")) {
			ADMINBranch();
		}
		try	{
			Statement stmt = conn.createStatement();
			String query = "UPDATE tbl_library_branch SET branchAddress = '"+branchAdd+"' WHERE branchId = "+branchId+"";
			int num = stmt.executeUpdate(query);
			System.out.println(num + " address added");
			ADMINBranch();
		} catch (SQLException e)	{
			e.printStackTrace();
		}
	}
	
	public static void updateBranch()	{
		System.out.println("You are about to update the details of an existing library branch. Enter quit at any point to skip the operation. Pick a branch from the menu below:");
		HashMap<Integer, String> branchList = new HashMap<>();
		HashMap<String, Integer> branchId = new HashMap<>();
		Integer branchSelect = null;
		int i=1;
		try {
			Statement stmt = conn.createStatement();
			String query = "SELECT DISTINCT branchName, branchId FROM tbl_library_branch;";
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next())	{
				String branchName = rs.getString("branchName");
				int Id = rs.getInt("branchId");
				branchList.put(i, branchName);
				branchId.put(branchName, Id);
				System.out.println(i + ". " + branchName);
				i++;
			}
		}catch (SQLException e)	{
				e.printStackTrace();
		}
		Scanner scan = new Scanner(System.in);
		String branchSelectString = scan.nextLine();
		if (branchSelectString.equals("quit")) ADMINBranch();
		else branchSelect = Integer.parseInt(branchSelectString);
		while (branchSelect < 1 || branchSelect >= i) {
			System.out.println("Invalid input! Try again!");
			branchSelect = scan.nextInt();
		}
		String branchNameSelected = branchList.get(branchSelect);
		int branchIdSelected = branchId.get(branchNameSelected);
		
		System.out.println("Enter the new name of the branch. Quit to go to next option");
		String newName = scan.nextLine();
		if (newName.equals("quit")) updateBranchAddress(branchIdSelected);
		try	{
			Statement stmt = conn.createStatement();
			String query = "UPDATE tbl_library_branch SET branchName = '"+newName+"' WHERE branchId = "+branchIdSelected+" ";
			int i1 = stmt.executeUpdate(query);
			System.out.println(i1 + " name(s) updated!");
			updateBranchAddress(branchIdSelected);
		} catch (SQLException e)	{
			e.printStackTrace();
		}
	}
	public static void updateBranchAddress(int branchId)	{
		System.out.println("Enter the new address of the selected branch. Quit to go previous menu");
		Scanner scan = new Scanner(System.in);
		String branchAdd = scan.nextLine();
		if (branchAdd.equals("quit")) ADMINBranch();
		try	{
			Statement stmt = conn.createStatement();
			String query = "UPDATE tbl_library_branch SET branchAddress = '"+branchAdd+"' WHERE branchId = "+branchId+"";
			int num = stmt.executeUpdate(query);
			System.out.println(num + " address added");	
			System.out.println("Operation successful. Going back.");
			ADMINBranch();
		} catch (SQLException e)	{
			e.printStackTrace();
		}
	}
	
	public static void deleteBranch()	{
		System.out.println("You are about to delete an existing branch's name! Pick an option in the menu below. Type quit to go back to previous menu!");
		HashMap<Integer, String> branchList = new HashMap<>();
		HashMap<String, Integer> branchId = new HashMap<>();
		Integer branchSelect = null;
		int i=1;
		try {
			Statement stmt = conn.createStatement();
			String query = "SELECT DISTINCT branchName, branchId FROM tbl_library_branch;";
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next())	{
				String pubName = rs.getString("branchName");
				int Id = rs.getInt("branchId");
				branchList.put(i, pubName);
				branchId.put(pubName, Id);
				System.out.println(i + ". " + pubName);
				i++;
			}
		}catch (SQLException e)	{
				e.printStackTrace();
		}
		Scanner scan = new Scanner(System.in);
		String branchSelectString = scan.nextLine();
		if (branchSelectString.equals("quit")) ADMINBranch();
		else branchSelect = Integer.parseInt(branchSelectString);
		while (branchSelect < 1 || branchSelect >= i) {
			System.out.println("Invalid input! Try again!");
			branchSelect = scan.nextInt();
		}
		String branchNameSelected = branchList.get(branchSelect);
		int branchIdSelected = branchId.get(branchNameSelected);
		
		System.out.println("You are about to delete the branch's name: " + branchNameSelected + ". Type yes to confirm operation. Press quit to go back to the previous menu!");
		String newBranchName = scan.nextLine();
		if (newBranchName.equals("quit")) ADMINBranch();
		else if (newBranchName.equals("yes"))	{
			try {
				String query = "DELETE FROM tbl_library_branch WHERE branchId = '" + branchIdSelected+"'";
				Statement stmt = conn.createStatement();
				int number = stmt.executeUpdate(query);
				System.out.println("Records updated: " + number);
				System.out.println("Operation successful. Returning back.");
				ADMINBranch();
			}catch (SQLException e)	{
				e.printStackTrace();
			}
		}
		else {
			System.out.println("Invalid input. Try again!");
			deleteBranch();
		}
	}
	
	public static void ADMINBorrower()	{
		System.out.println("You are about to make changes to the borrower daabase. Pick an option!");
		System.out.println("1. Add a new borrower");
		System.out.println("2. Update a borrower's name/address/phone");
		System.out.println("3. Delete a borrower");
		System.out.println("4. Quit to previous menu");
		
		Scanner scan = new Scanner(System.in);
		int num1 = scan.nextInt();
		while (num1 != 1 & num1 != 2 & num1 != 3 & num1 != 4)	{
			System.out.println("Invalid Input! Try again!");
			num1 = scan.nextInt();
		}
		
		switch (num1)	{
			case 1: addBorrower();
					break;
			case 2: updateBorrower();
					break;
			case 3: deleteBorrower();
			case 4: ADMIN();
					break;
		}
	}
	
	public static void addBorrower()	{
		System.out.println("You are about to add a borrower to the database. Enter quit at any time to go back to the previous menu. Enter borrower's name!");
		Scanner scan = new Scanner(System.in);
		String newBorrowerName = scan.nextLine();
		Integer borrowerId = null;
		if (newBorrowerName.equals("quit")) ADMINBorrower();
		try {
			Statement stmt = conn.createStatement();
			String query = "INSERT INTO tbl_borrower (name) VALUES ('"+newBorrowerName+"')";
			int num = stmt.executeUpdate(query);
			System.out.println("Records updated: " + num);
			query = "SELECT cardNo FROM tbl_borrower WHERE name = '"+newBorrowerName+"'";
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next())	{
				borrowerId = rs.getInt("cardNo");
			}
			System.out.println("Do you want to add more details about this borrower?");
			System.out.println("Type the borrower's phone number. Enter quit to go to next option");
			addBorrowerPhone(borrowerId);
			System.out.println("Enter the borrower's address. Enter quit to go back to previous menu!");
			addBorrowerAddress(borrowerId);
		}catch (SQLException e)	{
				e.printStackTrace();
		}
	}
	
	public static void addBorrowerAddress(int borrowerId)	{
		Scanner scan = new Scanner(System.in);
		String borrowerAdd = scan.nextLine();
		if (borrowerAdd.equals("quit")) ADMINBorrower();
		try	{
			Statement stmt = conn.createStatement();
			String query = "UPDATE tbl_borrower SET address = '"+borrowerAdd+"' WHERE cardNo = "+borrowerId+"";
			int num = stmt.executeUpdate(query);
			System.out.println(num + " address added");	
			System.out.println("Operation successful. Going back.");
			ADMINBorrower();
		} catch (SQLException e)	{
			e.printStackTrace();
		}
	}
	public static void addBorrowerPhone(int borrowerId)	{
		Scanner scan = new Scanner(System.in);
		String borrowerPhone = scan.nextLine();
		if (borrowerPhone.equals("quit")) {
			System.out.println("Enter the borrower's address. Enter quit to go back to previous menu!");
			addPubAddress(borrowerId);
		}
		try	{
			Statement stmt = conn.createStatement();
			String query = "UPDATE tbl_borrower SET phone = '"+borrowerPhone+"' WHERE cardNo = "+borrowerId+"";
			int num = stmt.executeUpdate(query);
			System.out.println(num + " phone added");	
		} catch (SQLException e)	{
			e.printStackTrace();
		}
	}
	
	public static void updateBorrower()	{
		System.out.println("You are about to update the details of an existing borrower. Enter quit at any point to skip the operation. Pick a borrower from the menu below:");
		HashMap<Integer, String> borrowerList = new HashMap<>();
		HashMap<String, Integer> borrowerId = new HashMap<>();
		Integer borrowerSelect = null;
		int i=1;
		try {
			Statement stmt = conn.createStatement();
			String query = "SELECT DISTINCT name, cardNo FROM tbl_borrower;";
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next())	{
				String borrowerName = rs.getString("name");
				int Id = rs.getInt("cardNo");
				borrowerList.put(i, borrowerName);
				borrowerId.put(borrowerName, Id);
				System.out.println(i + ". " + borrowerName);
				i++;
			}
		}catch (SQLException e)	{
				e.printStackTrace();
		}
		Scanner scan = new Scanner(System.in);
		String borrowerSelectString = scan.nextLine();
		if (borrowerSelectString.equals("quit")) ADMINBorrower();
		else borrowerSelect = Integer.parseInt(borrowerSelectString);
		while (borrowerSelect < 1 || borrowerSelect >= i) {
			System.out.println("Invalid input! Try again!");
			borrowerSelect = scan.nextInt();
		}
		String borrowerNameSelected = borrowerList.get(borrowerSelect);
		int borrowerIdSelected = borrowerId.get(borrowerNameSelected);
		
		System.out.println("Enter the new name of the borrower. Quit to go to next option");
		String newName = scan.nextLine();
		if (newName.equals("quit")) updateBorrowerAddress(borrowerIdSelected);
		try	{
			Statement stmt = conn.createStatement();
			String query = "UPDATE tbl_borrower SET name = '"+newName+"' WHERE cardNo = "+borrowerIdSelected+" ";
			int i1 = stmt.executeUpdate(query);
			System.out.println(i1 + " name(s) updated!");
			updateBorrowerAddress(borrowerIdSelected);
		} catch (SQLException e)	{
			e.printStackTrace();
		}
	}
	public static void updateBorrowerAddress(int borrowerId)	{
		System.out.println("Enter the new address of the selected borrower. Quit to go to next option");
		Scanner scan = new Scanner(System.in);
		String borrAdd = scan.nextLine();
		if (borrAdd.equals("quit")) updateBorrowerPhone(borrowerId);
		try	{
			Statement stmt = conn.createStatement();
			String query = "UPDATE tbl_borrower SET address = '"+borrAdd+"' WHERE cardNo = "+borrowerId+"";
			int num = stmt.executeUpdate(query);
			System.out.println(num + " address(es) added");	
			System.out.println("Operation successful. Goint to next option.");
			updateBorrowerPhone(borrowerId);
		} catch (SQLException e)	{
			e.printStackTrace();
		}
	}
	public static void updateBorrowerPhone(int borrowerId)	{
		System.out.println("Enter the new phone number of the selected borrower. Quit to go to go back ");
		Scanner scan = new Scanner(System.in);
		String borrPhone = scan.nextLine();
		if (borrPhone.equals("quit")) ADMINBorrower();
		try	{
			Statement stmt = conn.createStatement();
			String query = "UPDATE tbl_borrower SET phone = '"+borrPhone+"' WHERE cardNo = "+borrowerId+"";
			int num = stmt.executeUpdate(query);
			System.out.println(num + " phone(s) added");	
			ADMINBorrower();
		} catch (SQLException e)	{
			e.printStackTrace();
		}
	}
	
	public static void deleteBorrower()	{
		System.out.println("You are about to delete an existing borrower's name! Pick an option in the menu below. Type quit to go back to previous menu!");
		HashMap<Integer, String> borrowerList = new HashMap<>();
		HashMap<String, Integer> borrowerId = new HashMap<>();
		Integer borrowerSelect = null;
		int i=1;
		try {
			Statement stmt = conn.createStatement();
			String query = "SELECT DISTINCT name, cardNo FROM tbl_borrower;";
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next())	{
				String borrName = rs.getString("name");
				int Id = rs.getInt("cardNo");
				borrowerList.put(i, borrName);
				borrowerId.put(borrName, Id);
				System.out.println(i + ". " + borrName);
				i++;
			}
		}catch (SQLException e)	{
				e.printStackTrace();
		}
		Scanner scan = new Scanner(System.in);
		String borrSelectString = scan.nextLine();
		if (borrSelectString.equals("quit")) ADMINBorrower();
		else borrowerSelect = Integer.parseInt(borrSelectString);
		while (borrowerSelect < 1 || borrowerSelect >= i) {
			System.out.println("Invalid input! Try again!");
			borrowerSelect = scan.nextInt();
		}
		String pubNameSelected = borrowerList.get(borrowerSelect);
		int borrowerIdSelected = borrowerId.get(pubNameSelected);
		
		System.out.println("You are about to delete the author name: " + pubNameSelected + ". Type yes to confirm operation. Press quit to go back to the previous menu!");
		String newBorrName = scan.nextLine();
		if (newBorrName.equals("quit")) ADMINBorrower();
		else if (newBorrName.equals("yes"))	{
			try {
				String query = "DELETE FROM tbl_borrower WHERE cardNo = '" + borrowerIdSelected+"'";
				Statement stmt = conn.createStatement();
				int number = stmt.executeUpdate(query);
				System.out.println("Records updated: " + number);
				System.out.println("Operation successful. Returning back.");
				ADMINBorrower();
			}catch (SQLException e)	{
				e.printStackTrace();
			}
		}
		else {
			System.out.println("Invalid input. Try again!");
			deleteBorrower();
		}
	}
	
	public static void ADMINOverride()	{
		System.out.println("You are about to over-ride a due date to a user. Existing users who have not returned book are below--pick an option. Enter quit to go back!");
		HashMap<Integer, String> borrList = new HashMap<>();
		HashMap<String, Long> borrIds = new HashMap<>();
		Integer borrSelect = null;
		int i = 1;
		try {
			Statement stmt = conn.createStatement();
			String query = "SELECT DISTINCT borr.name, borr.cardNo FROM tbl_borrower borr JOIN tbl_book_loans loans ON loans.cardNo = borr.cardNo WHERE dateIn IS NULL;";
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next())	{
				String borrName = rs.getString("name");
				Long Id = rs.getLong("cardNo");
				borrList.put(i, borrName);
				borrIds.put(borrName, Id);
				System.out.println(i + ". " + borrName);
				i++;
			}
		}catch (SQLException e)	{
				e.printStackTrace();
		}
		if (i == 1) {
			System.out.println("Nobody has to return a book. Going back");
			ADMIN();
		}
		Scanner scan = new Scanner(System.in);
		String borrSelectString = scan.nextLine();
		if (borrSelectString.equals("quit")) ADMIN();
		else 	{
			borrSelect = Integer.parseInt(borrSelectString);
		}
		while (borrSelect < 1 || borrSelect > i)	{
			System.out.println("Illegal input. Try again!");
			borrSelect = scan.nextInt();
		}
		
		String borrowerNameSelected = borrList.get(borrSelect);
		Long borrowerIdSelected = borrIds.get(borrowerNameSelected);
		
		System.out.println("You have selected the borrower: " + borrowerNameSelected + " with card number: " + borrowerIdSelected + ". Enter quit to go back to previous menu!");
		System.out.println("Here is (are) the book(s) " + borrowerNameSelected + " hasn't returned. Pick a selection.");
		i=1;
		HashMap<Integer, String> bookList = new HashMap<>();
		HashMap<String, Integer> bookIds = new HashMap<>();
		HashMap<Integer, String> dateOuts = new HashMap<>();
		try {
			Statement stmt = conn.createStatement();
			String query = "SELECT book.title, book.bookId, loan.dateOut FROM tbl_book book JOIN tbl_book_loans loan ON book.bookId = loan.bookId WHERE cardNo = "+borrowerIdSelected+" AND dateIn IS NULL";
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next())	{
				String bookName = rs.getString("book.title");
				int bookId = rs.getInt("book.bookId");
				String dateOut = rs.getString("loan.dateOut");
				System.out.println(i + ". " + bookName + " which was checked out on " + dateOut);
				bookList.put(i, bookName);
				bookIds.put(bookName, bookId);
				dateOuts.put(i, dateOut);
				i++;
			}
		}catch (SQLException e)	{
				e.printStackTrace();
		}
		Integer bookSelect = null;
		String bookSelectString = scan.nextLine();
		if (bookSelectString.equals("quit")) ADMIN();
		else 	{
			bookSelect = Integer.parseInt(bookSelectString);
		}
		while (bookSelect < 1 || bookSelect >= i)	{
			System.out.println("Illegal input. Try again!");
			bookSelect = scan.nextInt();
		}
		String bookSelected = bookList.get(bookSelect);
		int bookIdSelected = bookIds.get(bookSelected);
		String dateOutSelected = dateOuts.get(bookSelect);
		System.out.println("Enter the new due-date (yyyy-MM-dd): ");
	
		String newDueDate = scan.nextLine();
		//Timestamp ts = Timestamp.valueOf(newDueDate);
		
		try {
			Statement stmt = conn.createStatement();
			//PreparedStatement stmt1 = conn.prepareStatement("UPDATE tbl_book_loans SET dueDate = ? WHERE dateIn IS NULL AND cardNo = "+borrowerIdSelected+" AND bookId = "+bookIdSelected+"");
			String query = "UPDATE tbl_book_loans SET dueDate = '"+newDueDate+"' WHERE dateIn IS NULL AND cardNo = "+borrowerIdSelected+" AND bookId = "+bookIdSelected+" AND dateOut = '"+dateOutSelected+"'";
			stmt.executeUpdate(query);
			//stmt1.setString(1, newDueDate);
			System.out.println("Update successful. Going back!");
			ADMIN();
		} catch (SQLException e)	{
				e.printStackTrace();
		}
		
		
	}
	
	public static void main(String[] args) throws SQLException {
		
		conn = jdbcConnection.getConnection();
		MAIN();
		
	}
}
