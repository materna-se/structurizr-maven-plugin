package io.github.stephanpirnbaum;

import com.structurizr.Workspace;
import com.structurizr.dsl.StructurizrDslParser;
import com.structurizr.view.ThemeUtils;
import io.github.stephanpirnbaum.structurizr.renderer.AbstractDiagramExporter;
import io.github.stephanpirnbaum.structurizr.renderer.mermaid.MermaidExporter;
import io.github.stephanpirnbaum.structurizr.renderer.plantuml.PlantUMLExporter;
import io.github.stephanpirnbaum.structurizr.renderer.plantuml.PlantumlLayoutEngine;
import io.github.stephanpirnbaum.structurizr.renderer.structurizr.StructurizrExporter;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.*;
import java.util.Optional;

/**
 * Maven Plugin to export Structurizr DSL diagrams as SVG.
 *
 * @author Stephan Pirnbaum
 */
@Mojo(name = "export-diagrams")
public class StructurizrExportMojo extends AbstractMojo {

    @Parameter(property = "workspace", required = true)
    private File workspace;

    @Parameter(property = "workspaceJson")
    private File workspaceJson;

    @Parameter(property = "outputDir", defaultValue = "${project.build.directory}/structurizr-diagrams")
    private File outputDir;

    @Parameter(property = "plantumlLayoutEngine", defaultValue = "GRAPHVIZ")
    private PlantumlLayoutEngine plantumlLayoutEngine;

    @Parameter(property = "diagramRenderer", defaultValue = "C4_PLANTUML")
    private DiagramRenderer diagramRenderer;

    @Parameter(property = "installBrowser", defaultValue = "true")
    private boolean installBrowser;

    @Override
    public void execute() throws MojoExecutionException {
        try {
            getLog().info("Parsing Structurizr DSL: " + workspace.getAbsolutePath());
            StructurizrDslParser parser = new StructurizrDslParser();
            parser.parse(workspace);
            Workspace workspace = parser.getWorkspace();

            ThemeUtils.loadThemes(workspace);

            AbstractDiagramExporter exportStrategy = switch (diagramRenderer) {
                case C4_PLANTUML -> new PlantUMLExporter(this.plantumlLayoutEngine);
                case MERMAID -> new MermaidExporter();
                case STRUCTURIZR -> new StructurizrExporter(this.installBrowser);
            };

            exportStrategy.export(workspace, Optional.ofNullable(workspaceJson), this.outputDir);
        } catch (Exception e) {
            throw new MojoExecutionException("Failed to export Structurizr diagrams", e);
        }
    }
}
