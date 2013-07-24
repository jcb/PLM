package lessons.sort.baseball.universe;

import jlm.core.model.Game;


public class BaseballField
{
	/**
	 * Create a new baseball field
	 * @param numberOfBases : number of bases you want in your field
	 * @param playerLocationAmount the amount of player locations available on the base
	 * @return a new BaseballField
	 */
	public static BaseballField create(int numberOfBases,int playerLocationAmount)
	{
		BaseballField field = new BaseballField(numberOfBases,playerLocationAmount);
		field.mix();
		return field;
	}
	
	private BaseballBase[] bases; // the bases which composed the field
	private int[] holeContainer; // the current location of the hole
	private BaseballMove lastMove; // the last move made on the field -- used for graphical purpose only

	/**
	 * BaseballField constructor
	 * @param numberOfBases : number of bases in your field
	 * @param playerLocationAmount the amount of player locations available on the base
	 */
	private BaseballField(int numberOfBases,int playerLocationAmount)
	{
		// creating the bases
		this.bases = new BaseballBase[numberOfBases];
		for (int j = 0 ; j < numberOfBases ; j++)
		{
			this.bases[j]=new BaseballBase(j,playerLocationAmount);
		}

		// add the hole
		this.bases[this.bases.length-1].setPlayer(0,new BaseballPlayer(-1));
		
		// initializing holeContainer
		this.holeContainer = new int[2];
		this.holeContainer[0] = this.bases.length-1;
		this.holeContainer[1] = 1;
		
		this.setLastMove(null);
	}
	
	/**
	 * Make a copy of the given object
	 * @return A copy of the given object
	 */
	public BaseballField copy() {
		int n = this.getLocationsAmount();
		BaseballField newField = new BaseballField(this.bases.length,n); // copy the "size" of the current BaseballField
		newField.setHoleContainer(this.holeContainer);	// copy the hole container
		// Copy the disposition of the players on the current field
		for ( int i = 0 ; i < this.bases.length ; i++ )
		{
			for ( int j = 0 ; j < n;j++)
			{
				newField.bases[i].setPlayer(j,this.bases[i].getPlayer(j));
			}
		}
		return newField;
	}
	
	/**
	 * Make a textual description of the differences between the caller and field
	 * @param otherField : the field with which you want to compare your current field 
	 */
	public String diffTo(BaseballField otherField) {
		
		if (getAmountOfBases() != otherField.getAmountOfBases())
			return Game.i18n.tr("Differing amount of bases: {0} vs {1}",getAmountOfBases(),otherField.getAmountOfBases());
			
		if (getLocationsAmount() != otherField.getLocationsAmount())
			return Game.i18n.tr("Differing amount of players: {0} vs {1}", getLocationsAmount(), otherField.getLocationsAmount());
		
		StringBuffer sb = new StringBuffer();
		for ( int i = 0 ; i < getAmountOfBases() ; i++)
			if ( !this.getBase(i).equals(otherField.getBase(i)))
			sb.append(Game.i18n.tr("Base #{0} differs: {1} vs {2}",this.getBase(i).toString(),otherField.getBase(i).toString()));

		return sb.toString();
	}

	public boolean equals(Object other) {
		if (other == null || !(other instanceof BaseballField) )
			return false;
		
		BaseballField otherField = (BaseballField) other;
		if (   this.holeContainer[0] != otherField.holeContainer[0]
			|| this.holeContainer[1] != otherField.holeContainer[1]
			|| this.getAmountOfBases() != otherField.getAmountOfBases()
			|| this.getLocationsAmount() == otherField.getLocationsAmount())
			
			return false;

		for ( int i = 0 ; i< this.bases.length ;i++ )
			if (! this.bases[i].equals(otherField.bases[i]))
				return false;
					
		return true;
	}
	
	/**
	 * Give the number of bases on your field
	 * @return the number of bases on your field
	 */
	public int getAmountOfBases() {
		return this.bases.length;
	}
	
	/**
	 * Give a specific base of the field
	 * @return the wanted base
	 */
	public BaseballBase getBase(int i) {
		return this.bases[i];
	}
	
	/**
	 * Return the color of the base located at baseIndex
	 * @param baseIndex the index of the wanted base
	 * @return the color of the player in base baseIndex at position playerLocation
	 * @throws IllegalArgumentException if you ask for a base which isn't in the range 0 to amountOfBases-1
	 */
	public int getBaseColor(int baseIndex) {
		if ( baseIndex < 0 || baseIndex>= this.bases.length)
		{
			throw new IllegalArgumentException("getBaseColor: you ask for a base "+baseIndex+" which isn't in the range 0 to getAmountOfBases()-1.");
		}
		return this.bases[baseIndex].getColor();
	}

	/**
	 * Return the index of the base where is hole is located
	 * @return the index of the base where is hole is located
	 */
	public int getHoleBase() {
		return this.holeContainer[0];
	}
	
	/**
	 * Return the position in the base where is hole is located
	 * @return the position in the base where is hole is located
	 */
	public int getHolePositionInBase(){
		return this.holeContainer[1];
	}

	/**
	 * Return the last move made on the field
	 * @return the last move made on the field
	 */
	public BaseballMove getLastMove() {
		return lastMove;
	}

	/**
	 * Give the amount of players locations available on each base of the field
	 * @return The amount of players locations available on each base of the field
	 */
	public int getLocationsAmount() {
		return this.bases[0].getLocationsAmount();
	}

	/**
	 * Return the color of the player in base baseIndex at position playerLocation
	 * @param baseIndex the index of the wanted base
	 * @param playerLocation the location ( between 0 and getLocationsAmount()-1 ) of the wanted player
	 * @return the color of the player in base baseIndex at position playerLocation
	 * @throws IllegalArgumentException if playerLocation isn't 0 or 1
	 */
	public int getPlayerColor(int baseIndex, int playerLocation)  {
		return this.bases[baseIndex].getPlayerColor(playerLocation);
	}
	
	/**
	 * Tell if every player of the base is on the base
	 * @return if every players of the base is on the base
	 * @param baseIndex the index of the base that you want to check
	 */
	public boolean isBaseSorted(int baseIndex) {
		boolean sw = true;
		int n = this.getLocationsAmount();
		if ( baseIndex == this.bases.length-1 )		// it's the base with only one player
		{
			for ( int i = 0 ; i < n && sw; i++)
			{
				sw =  ( this.bases[baseIndex].getPlayer(i).getColor() == 0 )
					||( this.bases[baseIndex].getPlayer(i).getColor()== this.bases[baseIndex].getColor() ) ;
			}
		}
		else	// it's a regular base
		{
			for ( int i = 0 ; i < n && sw; i++)
			{
				sw = ( this.bases[baseIndex].getPlayer(i).getColor() == this.bases[baseIndex].getColor() ) ;
			}
		}
		return sw;
	}
	
	/**
	 * Tell if every player of every base is on the right base
	 * @return if every player of every base is on the right base
	 */
	private boolean isSorted() {
		boolean sw = true;
		for ( int i = 0 ; i < this.bases.length && sw ; i++ )
		{
			sw = this.isBaseSorted(i);
		}
		return sw;
	}
	
	/**
	 * Mix the players between the different bases
	 */
	private void mix() {
		int m = this.getLocationsAmount();
		// mix the base
		do
		{		
			for (int i = 0 ; i < this.bases.length;i++)
			{
				this.swap(i,(int) (Math.random()*m),(int) (Math.random()*this.bases.length),(int) (Math.random()*m));
			}
		}
		while(this.isSorted());
		
		// update the holeContainer
		int n = this.getAmountOfBases();
		boolean found = false;
			for ( int i = 0 ; i < n && !found ; i++)
			{
				for ( int j = 0 ; j < m && !found; j++)
				{
					if ( this.bases[i].getPlayerColor(j)==-1)
					{
						found = true;
						this.holeContainer[0] = i;
						this.holeContainer[1] = j;
					}
				}	
			}	
	}

	/**
	 * Moves the specified player into the hole
	 * @throws InvalidMoveException in case baseSrc is not near the hole
	 */
	public void move(int indexBaseSrc, int playerLocation) 
	{
		if ( indexBaseSrc >= this.getAmountOfBases() || indexBaseSrc < 0)
		{
			throw new IllegalArgumentException("The base index must be between 0 and "+(this.getAmountOfBases()-1)+".\nUnfortunatly, "+indexBaseSrc+" isn't");
		}
		if ( playerLocation < 0 || playerLocation > this.getLocationsAmount()-1 )
		{
			throw new IllegalArgumentException("There isn't a position "+playerLocation+".\nIt must be between 0 and getLocationsAmount()-1.");
		}
		// must work only if the bases are next to each other
		if (		( this.holeContainer[0] == indexBaseSrc+1 )
				||  ( this.holeContainer[0] == indexBaseSrc-1 )
				||  ( this.holeContainer[0] == 0 && indexBaseSrc == this.getAmountOfBases()-1 )
				||  ( this.holeContainer[0] == this.getAmountOfBases()-1 && indexBaseSrc == 0 )
				||  ( this.holeContainer[0] == indexBaseSrc )
			)
		{
				this.setLastMove(
						new BaseballMove(indexBaseSrc, playerLocation, 
								this.holeContainer[0], this.holeContainer[1], 
								this.getPlayerColor(indexBaseSrc, playerLocation)));
			swap(indexBaseSrc, playerLocation, this.holeContainer[0], this.holeContainer[1]);
			this.holeContainer[0]= indexBaseSrc;
			this.holeContainer[1]= playerLocation;
			
		}
		else
		{
			throw new IllegalArgumentException("The player "+playerLocation+" from base "+indexBaseSrc+" can't move to base "+this.holeContainer[0]+" since it's a lazy guy and he doesn't want to travel more than one base length at once");
		}
	}

	/**
	 * Replace the current holeContainer by hole
	 * @param hole : the new holeContainer
	 */
	private void setHoleContainer(int[] hole) {
		this.holeContainer = new int[hole.length];
		for ( int i = 0 ; i < hole.length;i++)
		{
			this.holeContainer[i]=hole[i];
		}
	}

	/**
	 * Replace the current last move by baseballMove
	 * @param baseballMove : the new last move
	 */
	private void setLastMove(BaseballMove baseballMove) {
		this.lastMove = baseballMove;
	}

	/**
	 * Swap two players from two bases
	 * @param indexBaseSrc : the index of the source base
	 * @param playerSrc : the position of the player that you want to move from the source base
	 * @param indexBaseDst : the index of the destination base
	 * @param playerDst : the position of the player that you want to move from the destination base
	 */
	private void swap(int indexBaseSrc, int playerSrc, int indexBaseDst, int playerDst) {
		BaseballPlayer flyingMan = this.bases[indexBaseSrc].getPlayer(playerSrc);
		this.bases[indexBaseSrc].setPlayer(playerSrc,this.bases[indexBaseDst].getPlayer(playerDst));
		this.bases[indexBaseDst].setPlayer(playerDst, flyingMan);
	}

	/**
	 * Return a string representation of the field
	 * @return A string representation of the field
	 */
	public String toString()
	{
		String s = "";
		int n = this.getLocationsAmount();
		for (int i = 0 ; i < this.bases.length ; i++)
		{
			s+="- Base "+i+"\n";
			s+="  Color : "+this.bases[i].getColor()+"\n";
			for ( int j = 0 ; j < n ; j++)
			{ 
				s+="  Player "+j+" : "+this.bases[i].getPlayer(j).getColor()+"\n";
			}
		}
		return s;
	}



	

	
}
