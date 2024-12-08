public class DoubleDice {
    private static int multiplier;//current double amount
    private static int owner; //number representing who currently owns the dice

    public DoubleDice() {
        multiplier = 1; //initial double amount is always 1
        owner = 0; //0 signifies the dice doesn't have an owner yet
    }

    public static int getDouble(){
        return multiplier;  //getter to return multiplier
    }

    public void setDouble(int d){
        multiplier = d;
    }

    //function to increase multiplier as long as its below 32 (max double value)
    public void increaseDouble(){
        if(multiplier == 32){
            System.out.println("The double amount can't be increased further, remains at 32");
        }
        else{
            multiplier = multiplier*2;
        }
    }

    public void setOwner(int newOwner){
        owner = newOwner; //owner is either set to 1 (player 1) or 2 (player 2)
    }

    public static int getOwner(){
        return owner;
    }

}