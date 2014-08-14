def fstruct
try {
  fstruct = new File("info.txt").readLines()
} catch(all) {
  println "Can't read the info.txt file."
  return
}

// return if not enough information about file structure
if (fstruct.size() < 2) {
    println "Please make sure folder strucutre is valid in info.txt file."
    return
}

def src = fstruct[0] 
def des = fstruct[1]

println "Source file will be read from $src;"
println "Processed codes will be outputed to $des."

def input
try {
    input = new File(src).text
} catch (all) {
    println "Can't read source file: $src."
    return
}
//delete unneccessary spaces
def wdChars = /a-zA-Z0-9_$/
def spChars = /^\sa-zA-Z0-9_$/
def sspChars = /^a-zA-Z0-9_$/

// remove spaces around special characters
input = input.replaceAll(/\s*([$spChars]+)\s*/, {it[1]})
// remove extra spaces around variable name
input = input.replaceAll(/([$wdChars]+)\s+([$wdChars]+)/, {it[1] + " " + it[2]})

// shorten the variable name
vcount = 0;

def vars = []
// find non function declared variables
(input =~ /var ([$wdChars]+)=(?!function)/).each {vars.add(it[1])}

// find arguments for function
(input =~ /function\(([${wdChars},]+)\)/).each {
    it[1].split(",").each{ vars.add(it)}
}
// replace variable name to shorter version
vars.each{
    def var = getNextVarName(vcount++)
    input = input.replaceAll(/([$sspChars]+)$it(?=([^${wdChars}\(]+))/, {it[1] + var})
}

vars = []
// find function names
(input =~ /var ([$wdChars]+)=(?=function)/).each {vars.add(it[1])}
//replace function name to shorter version
vars.each{
    def var = getNextVarName(vcount++)
    input = input.replaceAll(/([$sspChars]+)$it(?=(\(|(=function)))/, {it[1] + var})
}

try {
    new File(des).setText(input)
} catch(all) {
    new File(des).setText(input)
} catch(all) {
    println "Can't open output file: $des."
    return
}

// get a valid variable name
def getNextVarName(ii) {
    def varNames = ('h'..'t') + ('z'..'u') + ('g'..'a');
    return varNames[ii]
}
