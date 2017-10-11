package game.model.ships;

/**
 * Created by amirshavit on 9/14/17.
 */
public class ShipType {

    private String typeId;
    private ShipCategory category;
    private int amount;
    private int length;
    private int score;

    public ShipType(String id, String category, int amount, int length, int score) {
        this.typeId = id;
        this.category = ShipCategory.valueOf(category);
        this.amount = amount;
        this.length = length;
        this.score = score;
    }

    //region Setters / Getters

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public ShipCategory getCategory() {
        return category;
    }

    public void setCategory(ShipCategory category) {
        this.category = category;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    //endregion
}
