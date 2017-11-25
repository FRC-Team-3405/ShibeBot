package frc.team3405.robot.lib.loopmanager

import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.launch

/**
 * Created by ryanberger on 11/25/17.
 */

object LoopManager {
    // lifecycle loops
    private val lifeCycleLoops: MutableList<LifeCycleLoop> = mutableListOf()
    private val runningLifeCycleLoops: MutableList<Job> = mutableListOf()

    // everything that is always running
    private val infiniteLoops: MutableList<Loop> = mutableListOf()
    private val runningInfiniteLoops: MutableList<Job> = mutableListOf()

    fun addLifeCycleLoop(loop: LifeCycleLoop) {
        lifeCycleLoops.add(loop)
    }

    fun addInfiniteLoop(loop: Loop) {
        infiniteLoops.add(loop)
    }

    fun robotInit() {
        // start all of our lifecycle magic
        runningInfiniteLoops += lifeCycleLoops.mapNotNull { it.robotInit }.map { launch { it() } }
        runningInfiniteLoops += infiniteLoops.map { launch { it() } }
    }

    fun startAutonomous() {
        runningLifeCycleLoops += lifeCycleLoops.mapNotNull { it.onAutonomous }.map { launch { it() } }
    }

    fun startTeleop() {
        runningLifeCycleLoops.forEach { it.cancel() }
        runningLifeCycleLoops.removeAll { true }
        runningLifeCycleLoops += lifeCycleLoops.mapNotNull { it.onTeleop }.map { launch { it() } }
    }

    fun disrupt() {

    }
}