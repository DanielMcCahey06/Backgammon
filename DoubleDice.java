public class DoubleDice {
    private int multiplier;//current double amount
    private int owner; //number representing who currently owns the dice

    public DoubleDice() {
        multiplier = 1; //initial double amount is always 1
        owner = 0; //0 signifies the dice doesnt have an owner yet
    }
    public int getDouble(){
        return multiplier;  //getter to return multiplier
    }
    //function to increase multiplier as long as its below 32 (max double value)
    public void increaseDouble(){
        if(multiplier == 32){
            System.out.println("The double amount can't be increased further, remains at 32");

        }
        else{
            multiplier = multiplier*2;}

    }
    public void setOwner(int newOwner){
        owner = newOwner; //owner is either set to 1 (player 1) or 2 (player 2)
    }
    public int getOwner(){
        return owner;
    }
}