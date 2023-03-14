package mac;

import javax.microedition.rms.*;



  /**
   * IntegerFilter class
   */
class IntegerFilter implements RecordFilter{

      /**
       * Used toward the end of this example, when creating a
       * RecordEnumerator to only index through the integer records.
       */
      public boolean matches(byte[] candidate)
             throws IllegalArgumentException {

          /*
           * Only show integers
           */
          return candidate[0] == 'I';
      }
}
