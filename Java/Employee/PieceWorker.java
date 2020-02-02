package q2;

//This class defines a type of worker called PieceWorker
public class PieceWorker extends Employee {
    private double piecePrice;
    private int manufacturedPieces;

    public PieceWorker(String firstName, String lastName, q2.BirthDate birthDate, String idNumber, double unitPrice, int manufacturedUnits) {
        super(firstName, lastName, idNumber, birthDate);
        if (piecePrice < 0.0) // Check
            throw new IllegalArgumentException("Piece price must be at least 0.0");
        piecePrice = unitPrice;
        manufacturedPieces = manufacturedUnits;
    }

    public double getPiecePrice() {
        return this.piecePrice;
    }

    public void setPiecePrice(double piecePrice) {
        this.piecePrice = piecePrice;
    }

    public int getManufacturedPieces() {
        return this.manufacturedPieces;
    }

    @Override
    public String toString() {
        return String.format("%s: %s%n%s: $%,.2f; %s: %d",
                "PieceWorker employee", super.toString(),
                "Piece price", this.getPiecePrice(),
                "Amount of pieces manufactured", this.getManufacturedPieces());
    }

    @Override
    public double earnings() {
        return this.getPiecePrice() * this.getManufacturedPieces();
    }

    @Override
    public void addToSalary(double amount) {
        this.setPiecePrice((this.getPiecePrice() + amount / this.getManufacturedPieces()));
    }
}
