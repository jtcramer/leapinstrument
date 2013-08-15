package com.jasoncramer.leapinstrument

import com.leapmotion.leap.{Controller, Listener}

case class Engine() {
  def run(): Unit = {
    val instrument = MainInstrument(0, new Instrument())
    val controller = new Controller(instrument)
  }
}