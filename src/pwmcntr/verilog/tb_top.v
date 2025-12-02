`timescale 1ns/1ps

module tb_top();
  
   reg clk;
   reg reset;
   reg io_pwm_clr_in;
   reg [11:0] io_div_config;
   reg [15:0] io_pwm_cc_in; 
   reg [15:0] io_pwm_top_in;

   wire io_pulse_o;

  initial begin
    clk = 0;
    forever #5 clk = ~clk;
  end

   PwmSystem dut(
    .io_pwm_top_in (io_pwm_top_in),
    .io_pwm_cc_in  (io_pwm_cc_in),
    .io_pwm_clr_in (io_pwm_clr_in),
    .io_div_config (io_div_config),
    .io_pulse_o    (io_pulse_o),
    .clk           (clk),
    .reset         (reset)
   );
   

      
     initial begin
        $dumpfile("pwm_wave.vcd");
        $dumpvars(0, tb_top);

        reset = 1;
        io_pwm_clr_in = 0;
        io_div_config = 0;
        io_pwm_cc_in = 0;
        io_pwm_top_in = 0;

        repeat(5) @(posedge clk);
        reset = 0;

        io_div_config = 37;
        io_pwm_clr_in = 1;

        @(posedge clk);

        io_pwm_clr_in = 0;

        io_pwm_cc_in = 5;
        io_pwm_top_in = 15;

        repeat(500) @(posedge clk);

        io_pwm_clr_in = 1;

        @(posedge clk);

        io_div_config = 0;

        io_pwm_clr_in = 0;

        io_pwm_cc_in = 1;
        io_pwm_top_in = 3;

        repeat(1500) @(posedge clk);

        $display("Testbench run successful");
        $finish;
     end

endmodule
