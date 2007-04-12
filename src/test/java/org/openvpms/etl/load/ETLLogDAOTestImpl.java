/*
 *  Version: 1.0
 *
 *  The contents of this file are subject to the OpenVPMS License Version
 *  1.0 (the 'License'); you may not use this file except in compliance with
 *  the License. You may obtain a copy of the License at
 *  http://www.openvpms.org/license/
 *
 *  Software distributed under the License is distributed on an 'AS IS' basis,
 *  WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 *  for the specific language governing rights and limitations under the
 *  License.
 *
 *  Copyright 2007 (C) OpenVPMS Ltd. All Rights Reserved.
 *
 *  $Id$
 */

package org.openvpms.etl.load;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * Add description here.
 *
 * @author <a href="mailto:support@openvpms.org">OpenVPMS Team</a>
 * @version $LastChangedDate: 2006-05-02 05:16:31Z $
 */
public class ETLLogDAOTestImpl implements ETLLogDAO {

    /**
     * The logs, keyed on log id.
     */
    private final Map<Long, ETLLog> logs = new HashMap<Long, ETLLog>();

    /**
     * Log identifier seed.
     */
    private long seed;

    /**
     * Saves a log.
     *
     * @param log the log to save
     */
    public void save(ETLLog log) {
        if (log.getLogId() == 0) {
            log.setLogId(++seed);
        }
        logs.put(log.getLogId(), log);
    }

    /**
     * Saves a collection of logs.
     *
     * @param logs the logs to save
     */
    public void save(Iterable<ETLLog> logs) {
        for (ETLLog log : logs) {
            save(log);
        }
    }

    /**
     * Returns an {@link ETLLog} given its identifier.
     *
     * @param logId the log identifier
     * @return the corresponding log, or <tt>null</tt> if none is found
     */
    public ETLLog get(long logId) {
        return logs.get(logId);
    }

    /**
     * Returns all {@link ETLLog}s associated with a loader, legacy identifier
     * and archetype.
     *
     * @param loader    the loader name. May be <tt>null</tt> to indicate all
     *                  loaders
     * @param legacyId  the legacy identifier.
     * @param archetype the archetype short name. May be <tt>null</tt> to
     *                  indicate all objects with the same legacy identifier
     * @return all logs matching the criteria
     */
    public List<ETLLog> get(String loader, String legacyId, String archetype) {
        List<ETLLog> result = new ArrayList<ETLLog>();
        for (ETLLog log : logs.values()) {
            if (loader != null && !log.getLoader().equals(loader)) {
                continue;
            }
            if (archetype != null && !log.getArchetype().equals(archetype)) {
                continue;
            }
            if (log.getRowId().equals(legacyId)) {
                result.add(log);
            }
        }
        return result;
    }

    /**
     * Deletes all {@link ETLLog}s associated with a loader and legacy
     * identifier.
     *
     * @param loader   the loader name
     * @param legacyId the legacy identifier
     */
    public void remove(String loader, String legacyId) {
        Iterator<ETLLog> iterator = logs.values().iterator();
        while (iterator.hasNext()) {
            ETLLog log = iterator.next();
            if (log.getRowId().equals(legacyId)
                    && log.getLoader().equals(loader)) {
                iterator.remove();
            }
        }
    }
}