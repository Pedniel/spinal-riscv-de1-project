package pwm
import spinal.core._
import spinal.lib._
import spinal.core.sim._

class FreqDiv() extends Component {
  
  val io = new Bundle {
    val div_in = in UInt(12 bits) setName("DIV_REGISTER")
    val en_o   = out Bool()       setName("EN_O")
  }

  val frac_acc = Reg(UInt(5 bits)) init(0)
  val div_int  = io.div_in(11 downto 4)
  val ctr      = Reg(UInt(8 bit)) init(0) setName("CTR")

  // FSM
  when (frac_acc.msb === True) {
    frac_acc.msb := False
    io.en_o := False
  } elsewhen (ctr === div_int) {
    ctr := 0
    io.en_o := True
    frac_acc := frac_acc(3 downto 0) +^ io.div_in(3 downto 0)
  } otherwise {
    io.en_o := False
    ctr := ctr + 1
  }
 
}

object MyFreqDivVerilog extends App {
  SpinalVerilog(new FreqDiv)
} 
