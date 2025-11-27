import spinal.core._
import spinal.lib._

// Register addr offest
object SigmaDeltaReg {
  val DIV  = 0x00  
  val INT  = 0x04  
}

case class SigDeltaFreqDiv(val int : UInt, val div : UInt) extends Component {
  import SigmaDeltaReg._
  val wordCount = (byteArray.length+3)/4 // Found in MuraxUtiles.scala
  val io = new Bundle{
    val apb = slave(Apb3(log2Up(wordCount*4),32)) // addressWidth, dataWidth
    val KEY0     = in Bool() setName("LED_KEY")
    val LEDR     = out Bits(8 bits) setName("LED_OUT")                                 
  }

  val apbWrite = io.apb.PSEL && io.apb.PENABLE && io.apb.PWRITE
  val apbRead  = io.apb.PSEL && io.apb.PENABLE && !io.apb.PWRITE

  val freq_int = Reg(UInt(16 bits)) init (0) setName("freq_int") 
  val freq_div = Reg(UInt(8 bits))  init (0) setName("freq_div")
  val freq_ctr = Reg(UInt(16 bits)) init (0) setName("freq_ctr")

  // Default value assignmnents
  io.LEDR := B"8'x00"
  io.apb.PRDATA := 0

  // Reg Addr offset mapping for READ and WRITE 
  when(apbWrite) {
    switch(io.apb.PADDR) {
      is(U(DIV, 4 bits)) {
        freq_div := io.apb.PWDATA(7 downto 0).asUInt
      }
      is(U(INT, 4 bits)) {
        freq_int := io.apb.PWDATA(15 downto 0).asUInt
      }
    }
  }

  when(apbRead) {
    switch(io.apb.PADDR) {
      is(U(DIV, 4 bits)) {
        io.apb.PRDATA := freq_div.asBits.resize(32)
      }
      is(U(INT, 4 bits)) {
        io.apb.PRDATA := freq_int.asBits.resize(32)
      }
    }
  }

  // LED Handling
  when(io.KEY0) {
    io.LEDR := B"8'xFF"
  }
}

object MyDesignVerilog extends App {
  SpinalVerilog(new SigDeltaFreqDiv)
} 

  /////////////////////////////////////////////////
  // Apb Summary for me :                         //
  //                                              //
  //  PSEL    : 1 = SELECTED                      //
  //                                              //
  //  PENABLE : 1 for DATA incoming               //
  //                                              //
  //  PWRITE  : 1 = Write, 0 = Read               //
  //                                              //
  //  PADDR   : Byte offset for Reg map           //
  //                                              //
  //  PWDATA  : Write -> Data from bus to me      //
  //                                              //
  //  PRDATA  : Read ->  Data from me to bus      //
  /////////////////////////////////////////////////

