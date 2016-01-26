package com.aivo.hyperion.aivo;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.aivo.hyperion.aivo.models.Line;
import com.aivo.hyperion.aivo.models.Magnet;
import com.aivo.hyperion.aivo.models.MagnetGroup;
import com.aivo.hyperion.aivo.models.Mindmap;
import com.aivo.hyperion.aivo.models.ModelListener;
import com.aivo.hyperion.aivo.models.ModelMediator;
import com.aivo.hyperion.aivo.models.User;
import com.aivo.hyperion.aivo.models.actions.ActionHandler;
import com.aivo.hyperion.aivo.models.actions.CreateMagnet;

import java.util.List;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    private MockListener listener1;
    private MockListener listener2;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        listener1 = new MockListener();
        listener2 = new MockListener();


        ModelMediator mediator = new ModelMediator();
        listener1.setMediator(mediator);
        listener2.setMediator(mediator);

        mediator.registerListener(listener1);
        mediator.registerListener(listener2);
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }


    /** User creation.
     *
     * @throws Exception
     */
    public void testCreateUser() throws Exception {

        listener1.getMediator().createUser();

//        wait(1000);
        assertEquals(true, listener1.getMediator().getUser() != null);
        assertEquals(true, listener1.getUser() != null);
        assertEquals(true, listener1.getUser() != null);
        assertEquals(true, listener1.getUser().getId() == listener2.getUser().getId());
    }

    /** Mindmap creation.
     *
     * @throws Exception
     */
    public void testCreateMindmap() throws  Exception {
        testCreateUser();

        listener1.getMediator().createMindmap();

        assertEquals(true, listener1.getMindmap() != null);
        assertEquals(true, listener2.getMindmap() != null);
        assertEquals(true, listener1.getMindmap().getId() == listener2.getMindmap().getId());
        assertEquals(true, listener1.getMindmap().getLines().isEmpty());
        assertEquals(true, listener1.getMindmap().getMagnetGroups().isEmpty());

        assertEquals(true, listener1.getMediator().getMindmap() == listener1.getMindmap());
    }

    /** Magnet creation.
     *
     * @throws Exception
     */
    public void testCreateMagnet() throws Exception {
        //testCreateUser();
        testCreateMindmap();

        listener1.getMindmap().createMagnet(50, 50);

        assertEquals(false, listener1.getMindmap().getMagnetGroups().isEmpty());
        MagnetGroup magnetGroup = listener1.getMindmap().getMagnetGroups().get(0);
        assertEquals(false, magnetGroup.getMagnets().isEmpty());
        assertEquals(true, magnetGroup.getX() == 50 && magnetGroup.getY() == 50);
        assertEquals(true, magnetGroup.getLines().isEmpty());
        assertEquals(true, magnetGroup.getTitle().isEmpty());
        Magnet magnet = magnetGroup.getMagnets().get(0);
    }

    /** Line creation between two magnet groups
     *
     * @throws Exception
     */
    public void testCreateLine() throws Exception {
        // need two magnets
        testCreateMagnet();
        listener1.getMindmap().createMagnet(70, 70);

        // get magnetgroups of two magnets
        List<MagnetGroup> magnetGroups = listener1.getMindmap().getMagnetGroups();
        assertEquals(true, magnetGroups.size() == 2);

        // create and check you cant duplicate lines
        listener1.getMindmap().createLine(magnetGroups.get(0), magnetGroups.get(1));
        listener1.getMindmap().createLine(magnetGroups.get(0), magnetGroups.get(1));
        assertEquals(true, listener1.getMindmap().getLines().size() == 1);
        assertEquals(true, magnetGroups.get(0).getLines().get(0) != null);
        assertEquals(true, magnetGroups.get(1).getLines().get(0) != null);

        Line line = listener1.getMindmap().getLines().get(0);
        assertEquals(true, magnetGroups.contains(line.getMagnetGroup1()));
        assertEquals(true, magnetGroups.contains(line.getMagnetGroup2()));
    }

    /** Move magnet from one group to another.
     *  Automatic removal of empty magnet group.
     *  Automatic removal of connected lines on group removal.
     *
     * @throws Exception
     */
    public void testMoveMagnet() throws Exception {
        // two groups each with one magnet, with a line in between
        testCreateLine();

        // move magnet from group 2 to group 1.
        listener1.getMindmap().getMagnetGroups().get(1).getMagnets().get(0).moveToMagnetGroup(
                listener1.getMindmap().getMagnetGroups().get(0)
        );

        // first group has two magnets, the empty group is removed -> connected line is also removed
        assertEquals(true, listener1.getMindmap().getMagnetGroups().get(0).getMagnets().size() == 2);
        assertEquals(true, listener1.getMindmap().getMagnetGroups().size() == 1);
        assertEquals(true, listener1.getMindmap().getLines().isEmpty());
    }

    public void testDraw() throws Exception{
        testCreateUser();
        testCreateMindmap();
        testCreateMagnet();


    }
}