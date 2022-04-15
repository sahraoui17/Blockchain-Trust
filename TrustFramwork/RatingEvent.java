/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TrustFramwork;

import org.cloudbus.cloudsim.core.SimEvent;
import org.cloudbus.cloudsim.core.*;

public class RatingEvent extends SimEvent{
   Transaction trans;
    Blockchain BC;

    RatingEvent(Transaction trans,Blockchain BC) {
        this.trans=trans;
        this.BC=BC;
    }

}
