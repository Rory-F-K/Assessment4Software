public class Book {
    private String bookID;
    private String bookTitle;
    private int bookQuantity;
    private double bookPrice;
    private String bookType;
}

public Book(String ID,String Title,int Quantity, double Price, String Type){
    bookID=ID;
    bookTitle=Title;
    bookQuantity=Quantity;
    bookPrice=Price;
    bookType=Type;
};

public String AddBook(){
    if((bookID.length()!=5)||(!Character.isDigit(bookID.charAt(0)))){
        return "The book information cannot be added";
    }
}



