// Generator : SpinalHDL v1.9.4    git head : 270018552577f3bb8e5339ee2583c9c22d324215
// Component : Modcntr
// Git hash  : 28c063ba47ef8dd35882ce44af030a77a5bc8197

`timescale 1ns/1ps

module Modcntr (
  input  wire [15:0]   _zz_top,
  output reg           _zz_1,
  input  wire          clk,
  input  wire          reset
);

  reg        [15:0]   top;
  reg        [15:0]   ctr;
  wire                when_modcnt_l17;
  wire                when_modcnt_l19;

  always @(*) begin
    _zz_1 = 1'b0;
    if(!when_modcnt_l17) begin
      if(when_modcnt_l19) begin
        _zz_1 = 1'b1;
      end
    end
  end

  assign when_modcnt_l17 = (top != _zz_top);
  assign when_modcnt_l19 = (ctr == _zz_top);
  always @(posedge clk or posedge reset) begin
    if(reset) begin
      top <= 16'h0000;
      ctr <= 16'h0000;
    end else begin
      top <= _zz_top;
      if(when_modcnt_l17) begin
        ctr <= 16'h0000;
      end else begin
        if(when_modcnt_l19) begin
          ctr <= 16'h0000;
        end else begin
          ctr <= (ctr + 16'h0001);
        end
      end
    end
  end


endmodule
