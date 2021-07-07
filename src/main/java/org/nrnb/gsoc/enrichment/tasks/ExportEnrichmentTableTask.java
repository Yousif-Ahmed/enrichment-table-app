package org.nrnb.gsoc.enrichment.tasks;


import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyTable;
import org.cytoscape.service.util.CyServiceRegistrar;
import org.cytoscape.task.write.ExportTableTaskFactory;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.ProvidesTitle;
import org.cytoscape.work.TaskIterator;
import org.cytoscape.work.TaskMonitor;
import org.cytoscape.work.Tunable;
import org.cytoscape.work.util.ListMultipleSelection;


import org.nrnb.gsoc.enrichment.ui.EnrichmentCytoPanel;

public class ExportEnrichmentTableTask extends AbstractTask {

    private EnrichmentCytoPanel enrichmentPanel;
    private CyTable selectedTable;

    @Tunable(description = "Save Table as", params = "input=false",
            tooltip="<html>Note: for convenience spaces are replaced by underscores.</html>", gravity = 2.0)
    public File fileName = null;

    final CyServiceRegistrar registrar;
    public ExportEnrichmentTableTask(CyServiceRegistrar registrar, CyNetwork network, EnrichmentCytoPanel panel, CyTable table, boolean filtered) {
        this.registrar = registrar;
        this.enrichmentPanel = panel;
        this.selectedTable = table;
    }

    @Override
    public void run(TaskMonitor taskMonitor) throws Exception {
        taskMonitor.setTitle("Export gProfiler Enrichment table");
        ExportTableTaskFactory exportTF = registrar.getService(ExportTableTaskFactory.class);

        if (selectedTable != null && fileName != null) {
            File file = fileName;
            if (enrichmentPanel != null) {
                selectedTable = enrichmentPanel.getFilteredTable();
            }
            taskMonitor.showMessage(TaskMonitor.Level.INFO,
                    "export table " + selectedTable + " to " + file.getAbsolutePath());
            TaskIterator ti = exportTF.createTaskIterator(selectedTable, file);
            insertTasksAfterCurrentTask(ti);
        }
    }

    @ProvidesTitle
    public String getTitle() {
        return "Export gProfiler Enrichment table";
    }
}