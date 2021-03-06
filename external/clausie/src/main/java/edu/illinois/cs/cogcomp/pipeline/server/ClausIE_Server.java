/**
 * This software is released under the University of Illinois/Research and Academic Use License. See
 * the LICENSE file in the root folder for details. Copyright (c) 2016
 *
 * Developed by: The Cognitive Computation Group University of Illinois at Urbana-Champaign
 * http://cogcomp.org/
 */
package edu.illinois.cs.cogcomp.pipeline.server;

import edu.illinois.cs.cogcomp.annotation.AnnotatorException;
import edu.illinois.cs.cogcomp.pipeline.ClausIE_AnnotatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ClausIE_Server {
    private static Logger logger = LoggerFactory.getLogger(ExternalAnnotatorsServer.class);
    public static void main(String[] args) throws IOException, AnnotatorException {
        ClausIE_AnnotatorService.initialize();
        logger.info("Starting create the externals annotators pipeline . . . ");
        logger.info("Setting the service . . . ");
        ExternalAnnotatorsServer.setAnnotatorService(ClausIE_AnnotatorService.service);
        logger.info("Start the server . . . ");
        ExternalAnnotatorsServer.startServer(args, logger);
    }
}
