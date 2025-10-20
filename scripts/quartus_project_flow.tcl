## ----------------------------------------------------------------------------
## Script     : quartus_project_flow.tcl
## ----------------------------------------------------------------------------
## Author     : Johann Faerber, F. Beckmann
## Company    : University of Applied Sciences Augsburg
## ----------------------------------------------------------------------------
## Description: executes process steps in a quartus project
##              depending on the parameter process
##              expects project name as command line parameter 
##              e.g.
##              quartus_sh -t quartus_project_flow.tcl -projectname de1_mux2to1
##                         -process compile
## ----------------------------------------------------------------------------
## Revisions  : see end of file
## ----------------------------------------------------------------------------

package require cmdline
# Load Quartus II Tcl Project package
package require ::quartus::project

# ----------------------------------------------------------------------------
# Declare command line parameters
# ----------------------------------------------------------------------------
set parameters {
    {projectname.arg ""   "Project Name"}
}
array set arg [::cmdline::getoptions argv $parameters]

# ----------------------------------------------------------------------------
# Verify required paramters
# ----------------------------------------------------------------------------
set requiredParameters {projectname}
foreach parameter $requiredParameters {
    if {$arg($parameter) == ""} {
        puts stderr "Missing required parameter: -$parameter"
        exit 1
    }
}

# ----------------------------------------------------------------------------
# Check, if project exists
# ----------------------------------------------------------------------------
if { ![project_exists $arg(projectname)] } {
    post_message -type error "Project $arg(projectname) does not exist"
    exit
}
	# ----------------------------------------------------------------------------
	# Open project
	# ----------------------------------------------------------------------------
	project_open $arg(projectname)

    # ----------------------------------------------------------------------------
    # Run specified design flow by parameter -process
    # ----------------------------------------------------------------------------
    load_package flow

    execute_flow -compile

	# ----------------------------------------------------------------------------
    # Write Reports
	# ----------------------------------------------------------------------------
    load_package report
    load_report $arg(projectname)
    write_report_panel -file flowsummary.log "Flow Summary"

	# ----------------------------------------------------------------------------
	# Close project
	# ----------------------------------------------------------------------------
	project_close


