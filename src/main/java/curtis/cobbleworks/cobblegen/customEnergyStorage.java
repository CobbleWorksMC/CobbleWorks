package curtis.cobbleworks.cobblegen;

import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

public class customEnergyStorage extends EnergyStorage implements IEnergyStorage {

	public customEnergyStorage(int capacity) {
		super(capacity);
	}
	
	public customEnergyStorage(int capacity, int maxTransfer) {
		
        super(capacity, maxTransfer, maxTransfer);
    }

    public customEnergyStorage(int capacity, int maxReceive, int maxExtract) {
        super(capacity, maxReceive, maxExtract);
    }
    
    /*
     * Adding back the function to directly set the energy stored.
     * Use at your own risk.
     * 
     * Sets the energy stored amount to energyIn if simulate is set to false.
     */
    
    public void setEnergyStored(int energyIn, boolean simulate) {
    	if (!simulate) {
    		this.energy = energyIn;
    	}
    }
}
