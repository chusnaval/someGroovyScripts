def directoryName = "C:\\ideas\\Produccion\\src\\geo\\resources"
def fileSubStr = "e[ns].properties[^svn]"
def filePattern = ~/${fileSubStr}/
def directory = new File(directoryName)


//System.getProperty("line.separator")
class Tester {
    def ES_PATTERN = "_es"
    def EN_PATTERN = "_en"
    boolean append = true;
    def inexistentFile = new FileWriter("C:\\logInexistentFiles.txt", append);
    BufferedWriter inexistentFileBuffer = new BufferedWriter(inexistentFile)
    def stupidLinesFile = new FileWriter("C:\\logStupidLines.txt", append);
    BufferedWriter stupidLinesFileBuffer = new BufferedWriter(stupidLinesFile)
    def inexistentLinesFile = new FileWriter("C:\\logInexistentLines.txt", append);
    BufferedWriter inexistentLinesFileBuffer = new BufferedWriter(inexistentLinesFile)
    def sameValuesFile = new FileWriter("C:\\logSameValuesFile.txt", append);
    BufferedWriter sameValuesFileBuffer = new BufferedWriter(sameValuesFile)
    def valuesFiles = new FileWriter("C:\\logValuesFiles.txt", append);
    BufferedWriter valuesFilesBuffer = new BufferedWriter(valuesFiles)

    

    def close(){
        inexistentFileBuffer.flush()
        stupidLinesFileBuffer.flush()
        inexistentLinesFileBuffer.flush()
        sameValuesFileBuffer.flush()
        valuesFilesBuffer.flush()
        
        inexistentFileBuffer.close()
        stupidLinesFileBuffer.close()
        inexistentLinesFileBuffer.close()
        sameValuesFileBuffer.close()
        valuesFilesBuffer.close()        
        
        inexistentFile.close()
        stupidLinesFile.close()
        inexistentLinesFile.close()
        sameValuesFile.close()
        valuesFiles.close()
    }
    
   def findInAllLanguages(path){   
        assertFileExistence(replacePatterns(path));
    }
    
    def assertFileExistence(path){       
        def first = new File(path);
        if(!first.exists() || !first.canRead()){
            inexistentFile.write("\n\t${path} : not exits: YOUR APP IS BROKEN!!!");
        }

    }
    
    def evaluateKeysAndValues(path){
        findSameKeysInAllLanguages(path);
        findNotValidPairOfKeysAndValue(path);
        if(path.endsWith(ES_PATTERN+".properties")){
                findSameValueForAKeyInAllLanguages(path, replacePatterns(path));
        }

    }
    
    def findNotValidPairOfKeysAndValue(path){
       new File(path).eachLine{
            line -> if(!line.isEmpty() && !line.contains("=")){
                stupidLinesFile.write("\n\t${path} contains this stupid line ${line}");
            }
        }             
    }
        
    def findSameKeysInAllLanguages(path){
        new File(path).eachLine{
            line -> testKeyInOthersLanguages(path,line);
        }            
    }

    def testKeyInOthersLanguages(file, str){
        boolean founded = false;
        boolean isAKey = false;
                

        if(str.contains("=")  && !str.endsWith("=")){
               
            new File(replacePatterns(file)).eachLine{
                line -> if(line.contains("=") && !line.endsWith("="))
                        {
                            isAKey = true;

                            if(line.substring(0,line.indexOf("=")).equals(str.substring(0,str.indexOf("="))))
                            {
                                founded = true;
                            }             
                        }          
            }    
            
        }
        
        if(isAKey && !founded){
           inexistentLinesFile.write("\n\t${str} is not found in " + replacePatterns(file));
        }
    }


    def findSameValueForAKeyInAllLanguages(path, inverse){
        new File(path).eachLine{
            line -> testSameValueInOthersLanguages(path,line);
        }  
    }
    
    def testSameValueInOthersLanguages(file, str){
        boolean founded = false;
        boolean isAKey = false;
                

        if(str.contains("=")  && !str.endsWith("=")){
               
            new File(replacePatterns(file)).eachLine{
                line -> if(line.contains("=") && !line.endsWith("="))
                        {
                            isAKey = true;

                            if(line.equals(str))
                            {
                                founded = true;
                            }             
                        }          
            }    
            
        }
        
        if(isAKey && founded){
           sameValuesFile.write("\n\t${str} has same value in " + replacePatterns(file) + " and " + file);
        }
    }
    
    def replacePatterns(str){
        if(str.endsWith(ES_PATTERN+".properties"))
        {
            return str.replaceAll(ES_PATTERN+".properties", EN_PATTERN+".properties");
        }
        else if(str.endsWith(EN_PATTERN+".properties")){
            return str.replaceAll(EN_PATTERN+".properties", ES_PATTERN+".properties");
        }else{
            return str;
        }
    }
    
    def printAllNoti18nFiles(path){
        if(!path.endsWith(ES_PATTERN+".properties") && !path.endsWith(EN_PATTERN+".properties"))
        {
                valuesFiles.write("\n\t${path} isn't a valid i18n file.");
        }

    }
}



def findFilenameClosure =
{
    Tester tester = new Tester();
    if (it.name.endsWith(".properties"))
    {
        tester.findInAllLanguages(it.absolutePath);
        tester.printAllNoti18nFiles(it.absolutePath);
        tester.evaluateKeysAndValues(it.absolutePath);
        tester.close();
        //acceder version tag y mostrar claves nuevas

    }
}



println "Matching Files:"
directory.eachFileRecurse(findFilenameClosure)
println "end"   