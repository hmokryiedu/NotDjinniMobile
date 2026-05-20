package not.djinni.presentation.navigation

import androidx.navigation3.runtime.NavBackStack
import not.djinni.presentation.navigation.controller.Screens
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class NavigationControllerTest {

    @Test
    fun replaceAll_chooseRole_hasSingleRouteInStack() {
        val controller = createController()

        controller.replaceAll(Screens.ChooseRole)

        assertEquals(listOf(Screens.ChooseRole), controller.stack.toList())
    }

    @Test
    fun popBackStack_onRoot_callsOnRootBack() {
        val rootBackCalls = RootBackCalls()
        val controller = createController(rootBackCalls::invoke)

        val popped = controller.popBackStack()

        assertFalse(popped)
        assertEquals(1, rootBackCalls.count)
        assertEquals(listOf(Screens.Splash), controller.stack.toList())
    }

    @Test
    fun popBackStack_withChild_removesOnlyLastRoute() {
        val controller = createController()
        controller.replaceAll(Screens.Auth)
        controller.navigate(Screens.ChooseRole)

        val popped = controller.popBackStack()

        assertTrue(popped)
        assertEquals(listOf(Screens.Auth), controller.stack.toList())
    }

    @Test
    fun popBackStack_withChild_doesNotCallOnRootBack() {
        val rootBackCalls = RootBackCalls()
        val controller = createController(rootBackCalls::invoke)
        controller.replaceAll(Screens.Auth)
        controller.navigate(Screens.ChooseRole)

        val popped = controller.popBackStack()

        assertTrue(popped)
        assertEquals(0, rootBackCalls.count)
        assertEquals(listOf(Screens.Auth), controller.stack.toList())
    }

    @Test
    fun replaceAll_auth_clearsProtectedHistory() {
        val controller = createController()
        controller.replaceAll(Screens.Seeker.Main)
        controller.navigate(Screens.Seeker.Profile)

        controller.replaceAll(Screens.Auth)

        assertEquals(listOf(Screens.Auth), controller.stack.toList())
    }

    @Test
    fun popUpTo_whenAnchorExists_mutatesStackAndAddsKey() {
        val controller = createController()
        controller.replaceAll(Screens.Employer.Main)
        controller.navigate(Screens.Employer.CreateVacancy())

        val changed = controller.popUpTo(
            key = Screens.Employer.VacancyDetails(vacancyId = 42L),
            to = Screens.Employer.Main,
            inclusive = false,
        )

        assertTrue(changed)
        assertEquals(
            listOf(
                Screens.Employer.Main,
                Screens.Employer.VacancyDetails(vacancyId = 42L)
            ),
            controller.stack.toList()
        )
    }

    @Test
    fun popUpTo_whenAnchorIsTop_appendsKeyWithoutRemovingAnchor() {
        val controller = createController()
        controller.replaceAll(Screens.Employer.Main)

        val changed = controller.popUpTo(
            key = Screens.Employer.CreateVacancy(),
            to = Screens.Employer.Main,
            inclusive = false,
        )

        assertTrue(changed)
        assertEquals(
            listOf(
                Screens.Employer.Main,
                Screens.Employer.CreateVacancy()
            ),
            controller.stack.toList()
        )
    }

    @Test
    fun popUpTo_inclusiveRoot_keepsAnchorBeforeKey() {
        val controller = createController()
        controller.replaceAll(Screens.Employer.Main)

        val changed = controller.popUpTo(
            key = Screens.Employer.VacancyDetails(vacancyId = 42L),
            to = Screens.Employer.Main,
            inclusive = true,
        )

        assertTrue(changed)
        assertEquals(
            listOf(
                Screens.Employer.Main,
                Screens.Employer.VacancyDetails(vacancyId = 42L)
            ),
            controller.stack.toList()
        )
    }

    @Test
    fun popUpTo_withDuplicateAnchors_usesLastAnchor() {
        val controller = createController()
        controller.replaceAll(Screens.Seeker.Main)
        controller.navigate(Screens.Seeker.Profile)
        controller.navigate(Screens.Seeker.Main)
        controller.navigate(Screens.Seeker.EditProfile)

        val changed = controller.popUpTo(
            key = Screens.Seeker.AppliedVacancies,
            to = Screens.Seeker.Main,
            inclusive = false,
        )

        assertTrue(changed)
        assertEquals(
            listOf(
                Screens.Seeker.Main,
                Screens.Seeker.Profile,
                Screens.Seeker.Main,
                Screens.Seeker.AppliedVacancies
            ),
            controller.stack.toList()
        )
    }

    @Test
    fun popUpTo_whenAnchorMissing_doesNotMutate() {
        val controller = createController()
        controller.replaceAll(Screens.Public.Main)
        controller.navigate(Screens.Public.VacancyDetails(vacancyId = 7L))
        val before = controller.stack.toList()

        val changed = controller.popUpTo(
            key = Screens.Auth,
            to = Screens.Seeker.Main,
            inclusive = false,
        )

        assertFalse(changed)
        assertEquals(before, controller.stack.toList())
    }

    private fun createController(onRootBack: () -> Unit = {}): NavigationController {
        return NavigationController(
            stack = NavBackStack(Screens.Splash),
            onRootBack = onRootBack,
        )
    }

    private class RootBackCalls {
        var count: Int = 0
            private set

        operator fun invoke() {
            count += 1
        }
    }
}
