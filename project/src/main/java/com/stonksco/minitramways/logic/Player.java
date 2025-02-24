package com.stonksco.minitramways.logic;

/**
 * Data of the player
 */
public class Player {

	private double SatisfactionAvg;
	private int Money = 350;

	public double GetSatisfactionAvg() {
		return this.SatisfactionAvg;
	}

	public void SetSatisfactionAvg(double satisfactionAvg) {
		this.SatisfactionAvg = satisfactionAvg;
	}

	public int GetMoney() {
		return this.Money;
	}

	
	public void SetMoney(int money) {
		this.Money = money;
	}


	public void AddMoney(int nb) {
		Money+=nb;
	}
}