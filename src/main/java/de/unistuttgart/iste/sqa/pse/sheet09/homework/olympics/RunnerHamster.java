package de.unistuttgart.iste.sqa.pse.sheet09.homework.olympics;

import de.hamstersimulator.objectsfirst.datatypes.Direction;
import de.hamstersimulator.objectsfirst.datatypes.Location;
import de.hamstersimulator.objectsfirst.external.model.Hamster;
import de.hamstersimulator.objectsfirst.external.model.Territory;

/**
 * This class represents a hamster that can take part in running events. It
 * provides all the necessary methods to navigate through a race course and
 * handles the energy management.
 *
 * Each hamster starts out with 20 energy points. Running hard consumes three
 * energy points and running steadily consumes one energy point. Running slowly
 * consumes no energy points.
 *
 */
public final class RunnerHamster extends Hamster {

	/* Class Invariants:
	 * The tactics must never be null.
	 * {@code energyRemaining} and {@code actionsTaken} must never be negative.
	 */
	protected RacePlan runningTactic;
	protected FeedingStrategy feedingTactic;

	private int energyRemaining;
	private int actionsTaken;

	/**
	 * Create a new instance of {@link RunnerHamster}.
	 *
	 * The newly created hamster has 20 energy points, not yet taken any actions,
	 * and instance of {@link FeedNeverStrategy} and {@link RunSlowlyRacePlan} as
	 * initial tactics.
	 *
	 * @param territory territory to create the new hamster in. Must not be null.
	 * @param location  location to create the new hamster at. Must not be null.
	 * @param direction direction for the new hamster to look towards. Must not be
	 *                  null.
	 */
	public RunnerHamster(final Territory territory, final Location location, final Direction direction) {
		super(territory, location, direction, 0);

		this.energyRemaining = 20;
		this.actionsTaken = 0;
		this.feedingTactic = new FeedNeverStrategy();
		this.runningTactic = new RunSlowlyRacePlan();
	}

	/**
	 * Take three steps at a cost of three energy points.
	 * <p>
	 * Requires that the hamster has least three energy points. Ensures that the
	 * hamster moves three steps. In case of walls, the hamster turns. Ensures that
	 * the hamster's energy point decrease by three.
	 *
	 * @throws IllegalStateException If the Precondition is violated, i.e if the
	 *                               Hamster has insufficient energy points left.
	 */
	public void runHard() {
		if (energyRemaining < 3) throw new IllegalStateException("There are not enough energy points left to do this.");
		energyRemaining -= 3;
		/*
		 * loop_invariant moves forward i times
		 * decreasing 3 - i
		 */
		for (int i = 0; i < 3; i++) {
			moveForward();
		}
	}

	/**
	 * Take two steps at a cost of a single energy point.
	 * <p>
	 * Requires that the hamster has least one energy point. Ensures that the
	 * hamster moves two steps. In case of walls, the hamster turns. Ensures that
	 * the hamster's energy point decrease by one.
	 *
	 * @throws IllegalStateException If the Precondition is violated, i.e if the
	 *                               Hamster has insufficient energy points left.
	 */
	public void runSteadily() {
		if (energyRemaining < 1) {
			throw new IllegalStateException("There are not enough energy points left to do this.");
		}
		energyRemaining -= 1;
		/*
		 * loop_invariant moves forward i times
		 * decreasing 2 - i
		 */
		for (int i = 0; i < 2; i++) {
			moveForward();
		}
	}

	/**
	 * Take one steps without any energy costs.
	 * <p>
	 * Ensures that the hamster moves one step. In case of walls, the hamster turns.
	 */
	public void runSlowly() {
		moveForward();
	}

	/**
	 * Use a feeding zone to regain energy.
	 * <p>
	 * Requires that the hamster stands on a feeding zone (check by calling
	 * isAtFeedingZone() beforehand). Ensures that the hamster picks up a grain and
	 * has an additional five energy points after execution.
	 *
	 * @throws IllegalStateException If the Precondition is violated, i.e if Hamster
	 *                               does not stand on a feeding zone.
	 */
	public void useFeedZone() {
		if (!grainAvailable()) {
			throw new IllegalStateException("You are not standing at a feed zone!");
		}
		pickGrain();
		energyRemaining += 5;
	}

	/**
	 * Returns the amount of energy points this hamster has left at the moment.
	 *
	 * @return amount of remaining energy points.
	 */
	public int getEnergyRemaining() {
		return this.energyRemaining;
	}

	/**
	 * Returns whether or not this hamster has finished the race and finds himself
	 * in the finishers zone.
	 *
	 * @return true, if this hamster is at the goal, false otherwise.
	 */
	public boolean hasFinished() {
		return (getLocation().getRow() >= 10
				&& getLocation().getColumn() > 5
				&& getLocation().getColumn() < 10);
	}

	/**
	 * Returns whether or not this hamster is situated on a feeding zone.
	 *
	 * @return true, if this hamster is at a feed zone, false otherwise.
	 */
	public boolean isAtFeedZone() {
		return grainAvailable();
	}

	/**
	 * The hamster executes the next action according to his racing and feeding
	 * strategies.
	 * <p>
	 * The hamster takes movement actions, unless it stands an a feeding zone and
	 * wants to eat.
	 * <p>
	 * Ensures that the hamster executes exactly one step according to their feedingTactic or
	 * runningTactic. Ensures that the actionsTaken counter increments by one.
	 */
	public void executeNextAction() {
		if (isAtFeedZone() && feedingTactic.isFeedingRequired()) {
			useFeedZone();
		} else {
			runningTactic.nextStep(this);
		}
		this.actionsTaken++;
	}

	/**
	 * Move one step towards the finish line.
	 *
	 * Ensures that the hamster turns and moves in such a way that the hamster is
	 * exactly one tile closer to the finish line after execution.
	 */
	private void moveForward() {
		if (!frontIsClear()) {
			turnLeft();
		}
		if (!frontIsClear()) {
			turnLeft();
			turnLeft();
		}
		move();
	}

	/**
	 * Sets this hamsters RacePlan to {@code tactics}.
	 *
	 * @param tactics the new RacePlan. Must not be null.
	 */
	public void setRacePlan(final RacePlan tactics) {
		if (tactics == null) {
			throw new IllegalArgumentException("New tactic is null, but must not be.");
		}
		this.runningTactic = tactics;
	}

	/**
	 * Sets this hamsters FeedingStrategy to {@code tactics}.
	 *
	 * @param tactics the new FeedingStrategy
	 */
	public void setFeedingTactics(final FeedingStrategy tactics) {
		if (tactics == null) {
			throw new IllegalArgumentException("New tactic is null, but must not be.");
		}
		this.feedingTactic = tactics;
	}

	/**
	 * Returns the amount of times {@code executeNextAction()} has been called on
	 * this hamster.
	 *
	 * @return amount of calls to {@code executeNextAction()}.
	 */
	public int getActionsTaken() {
		return this.actionsTaken;
	}
}
