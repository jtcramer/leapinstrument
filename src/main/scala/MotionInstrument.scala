package com.jasoncramer.instrument

import com.leapmotion.leap.{Finger, Frame, Hand, Leap, FingerList, HandList}
import scala.collection.JavaConversions._

class Instrument

/** An interface between the LeapMotion SDK and the sound synthesis
  * API.
  */
abstract class MotionInstrument(handID: Int, instrument: Instrument)
extends HasFingersListeners with HasHandListeners {

  /** Provide a Frame update to the instrument.
    *
    * Invokes all listeners that correspond to hands
    * and fingers.
    *
    * @return True if the hand is still valid
    */
  def update(frame: Frame): Boolean = {
    val hand = frame.hands.get(handID)
    if (hand.isValid) {
      val fingers = hand.fingers.toSeq
      for (handListener <- handListeners) handListener(hand)
      for (fingersListener <- fingersListeners) fingersListener(fingers)
      true
    } else false
  }
}

trait HasFingersListeners {
  type FingersListener = (Seq[Finger]) => Unit
  def fingersListeners: Seq[FingersListener]
}

trait HasHandListeners {
  type HandListener = (Hand) => Unit
  def handListeners: Seq[HandListener]
}

case class MainInstrument(handId: Int, instrument: Instrument)
extends MotionInstrument(handId, instrument) {

  // Define listeners for fingers
  val fingerTipProximityTo: FingersListener = (fingers) => {
    // APPROACH 1a:
    // Naive furthest points
    //
    // The intuition here is that a sphere where the diameter
    // is defined by a line segment drawn between the two
    // points that are furthest apart.
    //
    // Since n<=5, which is relatively small, at least for now,
    // we can get away with the naive O(n^2) solution

    var maxDist = Double.MinValue

    for {
      (finger, index) <- fingers.zipWithIndex
      if index < (fingers.size - 1)
      finger2 <- fingers.drop(index + 1)
      dist = finger.tipPosition.distanceTo(finger2.tipPosition)
    } if(dist > maxDist) maxDist = dist

  }

  // Define Listeners for a hand
  val yToPitch: HandListener = (hand) => {
    val y = hand.palmPosition.getY
    // TODO: define instrument interface before we can
    //       actually do anything here
  }

  val sphereTo: HandListener = (hand) => {
    val radius = hand.sphereRadius
  }


  def fingersListeners: Seq[FingersListener] = Seq()
  def handListeners: Seq[HandListener] = Seq(yToPitch)
}