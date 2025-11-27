// Register addr offest
object SigmaDeltaReg {
  val DIV  = 0x00  
  val INT  = 0x04  
}

case class SigDeltaFreqDiv(val int, div : Int) extends Component {
  import SigmaDeltaReg._
  val wordCount = (byteArray.length+3)/4 // Found in MuraxUtiles.scala
  val io = new Bundle{
    val apb = slave(Apb3(log2Up(wordCount*4),32)) // addressWidth, dataWidth
  }

  val apbWrite = io.apb.PSEL && io.apb.PENABLE && io.apb.PWRITE
  val apbRead  = io.apb.PSEL && io.apb.PENABLE && !io.apb.PWRITE

  val freq_int = Reg(Uint(16 bits)) init (0) setName("freq_int") 
  val freq_div = Reg(Uint(8 bits))  init (0) setName("freq_div")
  val freq_ctr = Reg(UInt(16 bits)) init (0) setName("freq_ctr")

  // Reg Addr offset mapping
  when(apbWrite) {
    switch(io.apb.PADDR) {
      is(DIV) { div := io.apb.PWDATA(8 downto 0) }
      is(INT) { int := io.apb.PWDATA(15 downto 0) }
    }

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

