package cc.stock.tracker.document;



import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Document
public class Portfolio {

    @Id
    private String id;

    private String userId;
    private String name;
    private HashSet<String> symbols; //list of symbols
    transient  private ArrayList<Position> positions;

    public double totalEquity, totalProfit, percentProfit;


    public Portfolio(String userId, String name) {
        this.name = name;
        this.userId = userId;
        this.positions = new ArrayList<>();
        this.symbols = new HashSet<>();
    }

    public ArrayList<Position> getPositions() {
        return positions;
    }

    public void setPositions(ArrayList<Position> positions) {
        this.positions = positions;
    }

    public void addSymbol(String symbol){
        this.symbols.add(symbol);
    }

    public void addSymbols(List<String> symbols){
        this.symbols.addAll(symbols);
    }

    public void removeSymbol(String symbol){
        this.symbols.remove(symbol);
    }

    public void removeSymbols(List<String> symbols){
        this.symbols.removeAll(symbols);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HashSet<String> getSymbols() {
        return this.symbols;
    }

    public void setSymbols(HashSet<String> symbols) {
        this.symbols = symbols;
    }

    public String getOwner() {
        return userId;
    }

    public void setOwner(String owner) {
        this.userId = owner;
    }

    @Override
    public String toString() {
        return "Portfolio{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", symbols=" + symbols.toString() +
                ", owner='" + userId + '\'' +
                '}';
    }
}
