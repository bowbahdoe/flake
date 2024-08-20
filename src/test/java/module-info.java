open module dev.mccue.flake.test {
    requires org.junit.jupiter.api;
    requires org.junit.platform.engine;
    requires org.junit.platform.commons;
    requires net.jqwik.api;
    requires dev.mccue.flake;

    exports dev.mccue.flake.test;
}