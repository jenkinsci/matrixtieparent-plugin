package matrixtieparent;

import hudson.Extension;
import hudson.Launcher;
import hudson.model.AbstractProject;
import hudson.model.Descriptor;
import hudson.tasks.BuildWrapper;
import hudson.tasks.BuildWrapperDescriptor;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import java.io.IOException;
import java.util.Map;

import org.kohsuke.stapler.DataBoundConstructor;

/**
 * {@link BuildWrapper} that manages the label that ties a parent
 * build to a computer.  The assignment of the label is done in 
 * the override for Queue.QueueDecisionHandler.
 *
 * @author Ken Bertelson
 */
public class BuildWrapperMtp extends BuildWrapper {
    /**
     * Note that a label's string can simply be the name of a node.
     */
    private String labelName;

    @DataBoundConstructor
    public BuildWrapperMtp(String labelName) {
        this.labelName = labelName;
    }

    public String getLabelName() {
        return this.labelName;  // just so I can place a breakpoint
    }

    @Override
    public Environment setUp(final AbstractBuild build, Launcher launcher, final BuildListener listener) throws IOException, InterruptedException {
        return new Environment() {
            @Override
            public void buildEnvVars(Map<String, String> env) {
                env.put("MATRIX_TIE_PARENT", labelName);
            }
        };
    }

    @Override
    public Descriptor<BuildWrapper> getDescriptor() {
        return DESCRIPTOR;
    }

    @Extension
    public static final DescriptorImpl DESCRIPTOR = new DescriptorImpl();

    public static final class DescriptorImpl extends BuildWrapperDescriptor {
        DescriptorImpl() {
            super(BuildWrapperMtp.class);
        }

        public String getDisplayName() {
            return Messages.Descriptor_DisplayName();
        }

        @Override
        public String getHelpFile() {
            return "/plugin/matrixtieparent/HelpFile.html";
        }

        @Override
        public boolean isApplicable(AbstractProject<?, ?> item) {
            // This got a compiler error:  return (item instanceof MatrixProject);
            return (item.getClass().getName().equals("hudson.matrix.MatrixProject"));
        }

    }
}
