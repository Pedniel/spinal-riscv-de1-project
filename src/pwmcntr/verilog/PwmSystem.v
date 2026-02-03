// Generator : SpinalHDL v1.10.2    git head : 279867b771fb50fc0aec21d8a20d8fdad0f87e3f
// Component : PwmSystem
// Git hash  : 08e6afdb2b2f58a244976b4d270b19f945359af1

`timescale 1ns/1ps

module PwmSystem (
  input  wire [15:0]   io_pwm_top_in,
  input  wire [15:0]   io_pwm_cc_in,
  input  wire          io_pwm_clr_in,
  input  wire [11:0]   io_div_config,
  output wire          io_pulse_o,
  input  wire          clk,
  input  wire          reset
);

  wire                divider_io_en_o;
  wire                pwm_1_io_pulse_o;

  FreqDiv divider (
    .io_div_in (io_div_config[11:0]), //i
    .io_en_o   (divider_io_en_o    ), //o
    .clk       (clk                ), //i
    .reset     (reset              )  //i
  );
  Pwm pwm_1 (
    .io_top_in  (io_pwm_top_in[15:0]), //i
    .io_cc_in   (io_pwm_cc_in[15:0] ), //i
    .io_clr_in  (io_pwm_clr_in      ), //i
    .io_en_in   (divider_io_en_o    ), //i
    .io_pulse_o (pwm_1_io_pulse_o   ), //o
    .clk        (clk                ), //i
    .reset      (reset              )  //i
  );
  assign io_pulse_o = pwm_1_io_pulse_o;

endmodule

module Pwm (
  input  wire [15:0]   io_top_in,
  input  wire [15:0]   io_cc_in,
  input  wire          io_clr_in,
  input  wire          io_en_in,
  output reg           io_pulse_o,
  input  wire          clk,
  input  wire          reset
);

  reg        [15:0]   ctr;
  wire                when_Pwm_l21;
  wire                when_Pwm_l23;
  wire                when_Pwm_l30;
  wire                when_Pwm_l34;
  wire                when_Pwm_l33;

  assign when_Pwm_l21 = (io_top_in < io_cc_in);
  always @(*) begin
    if(when_Pwm_l21) begin
      io_pulse_o = 1'b1;
    end else begin
      if(when_Pwm_l23) begin
        io_pulse_o = 1'b0;
      end else begin
        io_pulse_o = 1'b1;
      end
    end
  end

  assign when_Pwm_l23 = (io_cc_in <= ctr);
  assign when_Pwm_l30 = (io_clr_in == 1'b1);
  assign when_Pwm_l34 = (ctr == io_top_in);
  assign when_Pwm_l33 = (io_en_in == 1'b1);
  always @(posedge clk or posedge reset) begin
    if(reset) begin
      ctr <= 16'h0;
    end else begin
      if(when_Pwm_l30) begin
        ctr <= 16'h0;
      end else begin
        if(when_Pwm_l33) begin
          if(when_Pwm_l34) begin
            ctr <= 16'h0;
          end else begin
            ctr <= (ctr + 16'h0001);
          end
        end
      end
    end
  end


endmodule

module FreqDiv (
  input  wire [11:0]   io_div_in,
  output reg           io_en_o,
  input  wire          clk,
  input  wire          reset
);

  wire       [7:0]    _zz_when_FreqDiv_l19;
  wire       [7:0]    _zz_when_FreqDiv_l19_1;
  reg        [4:0]    frac_acc;
  reg        [7:0]    ctr;
  wire                when_FreqDiv_l16;
  wire                when_FreqDiv_l19;

  assign _zz_when_FreqDiv_l19 = (_zz_when_FreqDiv_l19_1 - 8'h01);
  assign _zz_when_FreqDiv_l19_1 = (io_div_in >>> 3'd4);
  assign when_FreqDiv_l16 = (frac_acc[4] == 1'b1);
  always @(*) begin
    if(when_FreqDiv_l16) begin
      io_en_o = 1'b0;
    end else begin
      if(when_FreqDiv_l19) begin
        io_en_o = 1'b1;
      end else begin
        io_en_o = 1'b0;
      end
    end
  end

  assign when_FreqDiv_l19 = (ctr == _zz_when_FreqDiv_l19);
  always @(posedge clk or posedge reset) begin
    if(reset) begin
      frac_acc <= 5'h0;
      ctr <= 8'h0;
    end else begin
      if(when_FreqDiv_l16) begin
        frac_acc <= {1'b0,frac_acc[3 : 0]};
      end else begin
        if(when_FreqDiv_l19) begin
          ctr <= 8'h0;
          frac_acc <= ({1'b0,frac_acc[3 : 0]} + {1'b0,io_div_in[3 : 0]});
        end else begin
          ctr <= (ctr + 8'h01);
        end
      end
    end
  end


endmodule
