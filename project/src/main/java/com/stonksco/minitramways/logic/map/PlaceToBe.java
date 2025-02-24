package com.stonksco.minitramways.logic.map;

import com.stonksco.minitramways.logic.Vector2;
import com.stonksco.minitramways.logic.people.People;

/**
 * Interface to represents a place where people can be
 */
public interface PlaceToBe {

	int Amount();

	// Add a person to the place
	void Enter(People p);

	// Remove a person from the place
	void Exit(People p);

    Vector2 GetCoordinates();
}