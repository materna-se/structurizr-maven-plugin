package io.github.stephanpirnbaum;

import io.github.stephanpirnbaum.structurizr.renderer.Renderer;
import io.github.stephanpirnbaum.structurizr.renderer.WorkspaceRenderer;
import io.github.stephanpirnbaum.structurizr.renderer.plantuml.PlantumlLayoutEngine;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.nio.file.Path;

/**
 * Maven Plugin to export Structurizr DSL diagrams as SVG.
 *
 * @author Stephan Pirnbaum
 */
@Mojo(name = "export-diagrams")
public class StructurizrExportMojo extends AbstractMojo {

    private final WorkspaceRenderer workspaceRenderer = new WorkspaceRenderer();

    @Parameter(property = "workspace", required = true)
    private Path workspace;

    @Parameter(property = "workspaceJson")
    private Path workspaceJson;

    @Parameter(property = "outputDir", defaultValue = "${project.build.directory}/structurizr-diagrams")
    private Path outputDir;

    @Parameter(property = "plantumlLayoutEngine")
    private PlantumlLayoutEngine plantumlLayoutEngine;

    @Parameter(property = "renderer")
    private Renderer renderer;

    @Parameter(property = "viewKey")
    private String viewKey;

    @Override
    public void execute() throws MojoExecutionException {
        try {
            this.workspaceRenderer.render(this.workspace, this.workspaceJson, this.outputDir, this.viewKey, this.renderer, this.plantumlLayoutEngine);
        } catch (Exception e) {
            throw new MojoExecutionException("Failed to export Structurizr diagrams", e);
        }
    }

}
