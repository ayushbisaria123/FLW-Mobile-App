package org.piramalswasthya.sakhi.configuration

import org.piramalswasthya.sakhi.model.*
import java.text.SimpleDateFormat
import java.util.*

class ToBeDeletedHBNCFormDataset(
//    context : Context,
    private val nthDay: Int, private val hbnc: HBNCCache? = null
) {

    companion object {
        private fun getLongFromDate(dateString: String?): Long {
            val f = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
            val date = dateString?.let { f.parse(it) }
            return date?.time ?: 0L
        }

        private fun getDateFromLong(dateLong: Long): String? {
            if(dateLong==0L) return null
            val cal = Calendar.getInstance()
            cal.timeInMillis = dateLong
            val f = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
            return f.format(cal.time)


        }
    }

    private fun FormInputOld.getPosition(): Int {
        return value.value?.let { entries?.indexOf(it)?.plus(1) } ?: 0
    }
    private fun FormInputOld.getStringFromPosition(position : Int): String? {
        return if (position == 0) null else entries?.get(position-1)
    }


    fun mapCardValues(hbnc: HBNCCache, user: UserCache) {
        hbnc.visitCard = HbncVisitCard(
            ashaName = ashaName.value.value,
            villageName = villageName.value.value,
            subCenterName = healthSubCenterName.value.value,
            blockName = blockName.value.value,
            motherName = motherName.value.value,
            fatherName = fatherName.value.value,
            dateOfDelivery = getLongFromDate(dateOfDelivery.value.value),
            placeOfDelivery = placeOfDelivery.getPosition(),
            babyGender = gender.getPosition(),
            typeOfDelivery = typeOfDelivery.getPosition(),
            stillBirth = stillBirth.getPosition(),
            startedBreastFeeding = startedBreastFeeding.getPosition(),
            dischargeDateMother = getLongFromDate(dateOfDischargeFromHospitalMother.value.value),
            dischargeDateBaby = getLongFromDate(dateOfDischargeFromHospitalBaby.value.value),
            weightInGrams = weightAtBirth.value.value?.toInt() ?: 0,
            registrationOfBirth = registrationOfBirth.getPosition(),
        )
    }

//    fun mapPartIValues(hbnc: HBNCCache) {
//        hbnc.part1 = HbncPartI(
//            babyAlive = babyAlive.getPosition(),
//            dateOfBabyDeath = getLongFromDate(dateOfBabyDeath.value.value),
//            timeOfBabyDeath = timeOfBabyDeath.value.value,
//            placeOfBabyDeath = placeOfBabyDeath.getPosition(),
//            otherPlaceOfBabyDeath = otherPlaceOfBabyDeath.value.value,
//            isBabyPreterm = babyPreterm.getPosition(),
//            gestationalAge = gestationalAge.getPosition(),
//            dateOfFirstExamination = getLongFromDate(dateOfBabyFirstExamination.value.value),
//            timeOfFirstExamination = timeOfBabyFirstExamination.value.value,
//            motherAlive = motherAlive.getPosition(),
//            dateOfMotherDeath = getLongFromDate(dateOfMotherDeath.value.value),
//            timeOfMotherDeath = timeOfMotherDeath.value.value,
//            placeOfMotherDeath = placeOfBabyDeath.getPosition(),
//            otherPlaceOfMotherDeath = otherPlaceOfMotherDeath.value.value,
//            motherAnyProblem = motherProblems.value.value,
//            babyFirstFed = babyFedAfterBirth.getPosition(),
//            otherBabyFirstFed = otherBabyFedAfterBirth.value.value,
//            timeBabyFirstFed = whenBabyFirstFed.value.value,
//            howBabyTookFirstFeed = howBabyTookFirstFeed.getPosition(),
//            motherHasBreastFeedProblem = motherHasBreastFeedProblem.getPosition(),
//            motherBreastFeedProblem = motherBreastFeedProblem.value.value,
//        )
//    }

    fun mapPartIIValues(hbnc: HBNCCache) {
        hbnc.part2 = HbncPartII(
            dateOfVisit = System.currentTimeMillis(),
            babyTemperature = babyTemperature.value.value,
            babyEyeCondition = babyEyeCondition.getPosition(),
            babyUmbilicalBleed = babyBleedUmbilicalCord.getPosition(),
            actionBabyUmbilicalBleed = actionUmbilicalBleed.getPosition(),
            babyWeight = babyWeight.value.value ?: "0",
            babyWeightMatchesColor = babyWeigntMatchesColor.getPosition(),
            babyWeightColorOnScale = babyWeightColor.getPosition(),
            allLimbsLimp = allLimbsLimp.getPosition(),
            feedLessStop = feedingLessStop.getPosition(),
            cryWeakStop = cryWeakStopped.getPosition(),
            dryBaby = babyDry.getPosition(),
            wrapClothCloseToMother = wrapClothKeptMother.getPosition(),
            exclusiveBreastFeeding = onlyBreastMilk.getPosition(),
            cordCleanDry = cordCleanDry.getPosition(),
            unusualInBaby = unusualWithBaby.getPosition(),
            otherUnusualInBaby = otherUnusualWithBaby.value.value,
        )
    }

    fun mapVisitValues(hbnc: HBNCCache) {
        hbnc.homeVisitForm = HbncHomeVisit(
            dateOfVisit = getLongFromDate(dateOfMotherDeath.value.value),
            babyAlive = babyAlive.getPosition(),
            numTimesFullMeal24hr = timesMotherFed24hr.value.value?.toInt() ?: 0,
            numPadChanged24hr = timesPadChanged.value.value?.toInt() ?: 0,
            babyKeptWarmWinter = babyKeptWarmWinter.getPosition(),
            babyFedProperly = babyBreastFedProperly.getPosition(),
            babyCryContinuously = babyCryContinuously.getPosition(),
            motherTemperature = motherBodyTemperature.value.value,
            foulDischargeFever = motherWaterDischarge.getPosition(),
            motherSpeakAbnormallyFits = motherSpeakAbnormalFits.getPosition(),
            motherLessNoMilk = motherNoOrLessMilk.getPosition(),
            motherBreastProblem = motherBreastProblem.getPosition(),
            babyEyesSwollen = babyEyesSwollen.getPosition(),
            babyWeight = babyWeight.value.value,
            babyTemperature = babyTemperature.value.value,
            babyYellow = yellowJaundice.getPosition(),
            babyImmunizationStatus = childImmunizationStatus.value.value,
            babyReferred = babyReferred.getPosition(),
            dateOfBabyReferral = getLongFromDate(dateOfBabyReferral.value.value),
            placeOfBabyReferral = placeOfBabyReferral.getPosition(),
            otherPlaceOfBabyReferral = otherPlaceOfBabyReferral.value.value,
            motherReferred = motherReferred.getPosition(),
            dateOfMotherReferral = getLongFromDate(dateOfMotherReferral.value.value),
            placeOfMotherReferral = placeOfMotherReferral.getPosition(),
            otherPlaceOfMotherReferral = otherPlaceOfMotherReferral.value.value,
            allLimbsLimp = allLimbsLimp.getPosition(),
            feedingLessStopped = feedingLessStop.getPosition(),
            cryWeakStopped = cryWeakStopped.getPosition(),
            bloatedStomach = bloatedStomach.getPosition(),
            coldOnTouch = childColdOnTouch.getPosition(),
            chestDrawing = childChestDrawing.getPosition(),
            breathFast = breathFast.getPosition(),
            pusNavel = pusNavel.getPosition(),
            sup = sup.getPosition(),
            supName = supName.value.value,
            supComment = supRemark.value.value,
            supSignDate = getLongFromDate(dateOfSupSig.value.value),
        )
    }

    fun setVillageName(village: String) {
        villageName.value.value = village
    }

    fun setBlockName(block: String) {
        blockName.value.value = block
    }

    fun setAshaName(userName: String) {
        ashaName.value.value = userName
    }

    private val titleHomeVisit = FormInputOld(
        inputType = InputType.HEADLINE,
        title = "Home Visit Form for newborn and mother care",
        required = false
    )
    private val healthSubCenterName = FormInputOld(
        inputType = InputType.EDIT_TEXT, title = "Health Subcenter Name ", required = false
    )
    private val phcName = FormInputOld(
        inputType = InputType.EDIT_TEXT, title = "P.H.C. Name ", required = false
    )
    private val motherName = FormInputOld(
        inputType = InputType.TEXT_VIEW, title = "Mother Name", required = false
    )
    private val fatherName = FormInputOld(
        inputType = InputType.TEXT_VIEW, title = "Father Name", required = false
    )

    private val dateOfDelivery = FormInputOld(
        inputType = InputType.DATE_PICKER,
        title = "Date of Delivery",
        max = System.currentTimeMillis(),
        min = 0,
        required = false
    )

    private val placeOfDelivery = FormInputOld(
        inputType = InputType.DROPDOWN, title = "Place of Delivery", entries = arrayOf(
            "House",
            "Health center",
            "CHC",
            "PHC",
        ), required = false
    )
    private val gender = FormInputOld(
        inputType = InputType.RADIO, title = "Baby Gender", entries = arrayOf(
            "Male",
            "Female",
            "Transgender",
        ), required = false
    )

    private val typeOfDelivery = FormInputOld(
        inputType = InputType.RADIO, title = "Type of Delivery", entries = arrayOf(
            "Cesarean",
            "Normal",
        ), required = false
    )
    private val startedBreastFeeding = FormInputOld(
        inputType = InputType.DROPDOWN, title = "Started Breastfeeding", entries = arrayOf(
            "Within an hour", "1 - 4 hours", "4.1 - 24 hours", "After 24 hours"
        ), required = false
    )
    private val weightAtBirth = FormInputOld(
        inputType = InputType.EDIT_TEXT,
        title = "Weight at birth ( grams )",
        etInputType = android.text.InputType.TYPE_CLASS_NUMBER or android.text.InputType.TYPE_NUMBER_VARIATION_NORMAL,
        required = false
    )
    private val dateOfDischargeFromHospitalMother = FormInputOld(
        inputType = InputType.DATE_PICKER,
        title = "Discharge Date of Mother",
        max = System.currentTimeMillis(),
        min = 0,
        required = false
    )
    private val dateOfDischargeFromHospitalBaby = FormInputOld(
        inputType = InputType.DATE_PICKER,
        title = "Discharge Date of Baby",
        max = System.currentTimeMillis(),
        min = 0,
        required = false
    )
    private val motherStatus = FormInputOld(
        inputType = InputType.RADIO, title = "Mother Status", entries = arrayOf(
            "Living",
            "Dead",
        ), required = false
    )
    private val registrationOfBirth = FormInputOld(
        inputType = InputType.RADIO, title = "Registration Of Birth", entries = arrayOf(
            "Yes",
            "No",
        ), required = false
    )
    private val childStatus = FormInputOld(
        inputType = InputType.RADIO, title = "Child Status", entries = arrayOf(
            "Living",
            "Dead",
        ), required = false
    )
    private val homeVisitDate = FormInputOld(
        inputType = InputType.RADIO, title = "Home Visit Date", entries = arrayOf(
            "1st Day",
            "3rd Day",
        ), required = false
    )
    private val childImmunizationStatus = FormInputOld(
        inputType = InputType.CHECKBOXES, title = "Child Immunization Status", entries = arrayOf(
            "BCG", "Polio", "DPT 1", "Hepatitis-B"
        ), required = false
    )
    private val birthWeightRecordedInCard = FormInputOld(
        inputType = InputType.RADIO,
        title = "Birth weight of the newborn recorded in Mother and Child Protection Card",
        entries = arrayOf(
            "Yes",
            "No",
        ),
        required = false
    )
    //////////////////// Part 1 ////////////////////////

    private val titleTrainingPart1 = FormInputOld(
        inputType = InputType.HEADLINE, title = "New Born First Training Part 1", required = false
    )

    private val timeOfDelivery = FormInputOld(
        inputType = InputType.TIME_PICKER, title = "Delivery time", required = false
    )
    private val dateOfCompletionOfPregnancy = FormInputOld(
        inputType = InputType.DATE_PICKER,
        title = "Date of completion of pregnancy",
        max = System.currentTimeMillis(),
        min = 0,
        required = false
    )

    private val weeksSinceBabyBorn = FormInputOld(
        inputType = InputType.EDIT_TEXT,
        title = "How many weeks have been born (if child is born in less that 35 weeks, pay attention)",
        etInputType = android.text.InputType.TYPE_CLASS_NUMBER or android.text.InputType.TYPE_NUMBER_VARIATION_NORMAL,
        required = false
    )
    private val dateOfFirstTraining = FormInputOld(
        inputType = InputType.DATE_PICKER,
        title = "Date and time of first training",
        max = System.currentTimeMillis(),
        min = 0,
        required = false
    )
    val motherAnyProblem = FormInputOld(
        inputType = InputType.DROPDOWN, title = "Does mother have any problem", entries = arrayOf(
            "Very Bleeding ",
            "Anesthesia/ Seizure outbreak",
        ), required = false
    )

    val babyFedAfterBirth = FormInputOld(
        inputType = InputType.DROPDOWN,
        title = "What was the baby fed after birth ",
        entries = arrayOf(
            "Mother Milk",
            "Water",
            "Honey",
            "Mishri water",
            "Goat Milk",
            "Other",
        ),
        required = false
    )

//    private val whenBabyFirstFed = FormInput(
//        inputType = InputType.EDIT_TEXT,
//        title = "When was the baby first breastfed ",
//        required = false
//    )

    private val howBabyTookFirstFeed = FormInputOld(
        inputType = InputType.DROPDOWN, title = "How did the baby breastfeed? ", entries = arrayOf(
            "Forcefully",
            "Weakly ",
            "Could not breastfeed but had to be fed with spoon",
            "Could neither breast-feed nor could take milk given by spoon",
        ), required = false
    )
    private val actionBreastFeedProblem = FormInputOld(
        inputType = InputType.EDIT_TEXT,
        title = "Any breastfeeding problem if yes write taken action",
        required = false
    )
    private val anyBreastFeedProblem = FormInputOld(
        inputType = InputType.EDIT_TEXT,
        title = "If there is any problem in breastfeeding",
        required = false
    )


    //////////////////////////// Part 2 /////////////////////////////////

    private val titleTrainingPart2 = FormInputOld(
        inputType = InputType.HEADLINE,
        title = "Baby first health check-up training Part 2",
        required = false
    )

    private val babyBodyTemperature = FormInputOld(
        inputType = InputType.EDIT_TEXT,
        title = "Measure and record baby body temperature, write action",
        required = false
    )

    private val babyEyeCondition = FormInputOld(
        inputType = InputType.RADIO, title = "Baby eye condition", entries = arrayOf(
            "Normal ", "Swelling", "oozing pus"
        ), required = false
    )
    private val babyBleedUmbilicalCord = FormInputOld(
        inputType = InputType.RADIO,
        title = "Is there bleeding from the baby umbilical cord ",
        entries = arrayOf(
            "Yes",
            "No",
        ),
        required = false
    )
    private val babyWeightColor = FormInputOld(
        inputType = InputType.RADIO, title = "Weighing machine scale color", entries = arrayOf(
            "Red", "Yellow", "Green"
        ), required = false
    )

    //////////////////////////// Part Baby Phy Con /////////////////////////////////

    private val titleBabyPhysicalCondition = FormInputOld(
        inputType = InputType.HEADLINE,
        title = "Enter the child physical condition",
        required = false
    )
    private val allLimbsLimp = FormInputOld(
        inputType = InputType.RADIO,
        title = "All limbs limp",
        entries = arrayOf("Yes", "No"),
        required = false,
    )
    private val feedingLessStop = FormInputOld(
        inputType = InputType.RADIO,
        title = "Feeding less/stop",
        entries = arrayOf("Yes", "No"),
        required = false,
    )
    private val notDrinkMilk = FormInputOld(
        inputType = InputType.RADIO,
        title = "Not drinking milk",
        entries = arrayOf("Yes", "No"),
        required = false,
    )
    private val crySlow = FormInputOld(
        inputType = InputType.RADIO,
        title = "Crying slow",
        entries = arrayOf("Yes", "No"),
        required = false,
    )
    private val notCry = FormInputOld(
        inputType = InputType.RADIO,
        title = "Not crying",
        entries = arrayOf("Yes", "No"),
        required = false,
    )
    private val lookedAfterRegularly = FormInputOld(
        inputType = InputType.RADIO,
        title = "Whether the newborn was being looked after regularly",
        entries = arrayOf("Yes", "No"),
        required = false,
    )
    private val wipedWithCleanCloth = FormInputOld(
        inputType = InputType.RADIO,
        title = "The child was wiped with a clean cloth",
        entries = arrayOf("Yes", "No"),
        required = false,
    )
    private val keptWarm = FormInputOld(
        inputType = InputType.RADIO,
        title = "The child is kept warm",
        entries = arrayOf("Yes", "No"),
        required = false,
    )
    private val givenBath = FormInputOld(
        inputType = InputType.RADIO,
        title = "The child was not given a bath",
        entries = arrayOf("Yes", "No"),
        required = false,
    )
    private val wrapClothKeptMother = FormInputOld(
        inputType = InputType.RADIO,
        title = "The child is wrapped in cloth and kept to the mother",
        entries = arrayOf("Yes", "No"),
        required = false,
    )
    private val onlyBreastMilk = FormInputOld(
        inputType = InputType.RADIO,
        title = "Started breastfeeding only/ only given breast milk",
        entries = arrayOf("Yes", "No"),
        required = false,
    )


    ////////////////////// Newborn first training (A) ask mother

    private val dateOfAshaVisit = FormInputOld(
        inputType = InputType.TEXT_VIEW, title = "Date of ASHA's visit", required = false
    )

    private val titleAskMotherA = FormInputOld(
        inputType = InputType.HEADLINE,
        title = "Newborn first training (A) Ask mother",
        required = false
    )

    val timesMotherFed24hr = FormInputOld(
        inputType = InputType.EDIT_TEXT,
        title = "How many times the mother feeds her stomach in 24 hours. Action – If the mother does not eat full stomach or eat less than 4 times, advise mother to do so",
        etInputType = android.text.InputType.TYPE_CLASS_NUMBER or android.text.InputType.TYPE_NUMBER_VARIATION_NORMAL,
        etMaxLength = 1,
        required = false
    )


    val timesPadChanged = FormInputOld(
        inputType = InputType.EDIT_TEXT,
        title = "How many pads have been changed in a day for bleeding? Action – If more than 2 pad, refer the mother to the hospital.",
        etInputType = android.text.InputType.TYPE_CLASS_NUMBER or android.text.InputType.TYPE_NUMBER_VARIATION_NORMAL,
        required = false
    )

    val babyKeptWarmWinter = FormInputOld(
        inputType = InputType.RADIO,
        title = "During the winter season, is the baby kept warm? (Closer to the mother, dressed well and wrapped). - If it is not being done, ask the mother to do it.",
        entries = arrayOf("Yes", "No"),
        required = false,
    )

    val babyBreastFedProperly = FormInputOld(
        inputType = InputType.RADIO,
        title = "Is the child breastfed properly? (Whenever feeling hungry or breastfeeding at least 7 – 8 times in 24 hours). Action – if it is not being done then ask the mother to do it. ",
        entries = arrayOf("Yes", "No"),
        required = false,
    )
    val babyCryContinuously = FormInputOld(
        inputType = InputType.RADIO,
        title = "Does the child cry continuously or urinate less than 6 times a day? Action – Advice the mother for breast-feeding",
        entries = arrayOf("Yes", "No"),
        required = false,
    )


    //////////////////// Part - B //////////////////

    private val titleHealthCheckUpMotherB = FormInputOld(
        inputType = InputType.HEADLINE, title = "(B) Health Checkup of mother", required = false
    )

    private val motherBodyTemperature = FormInputOld(
        inputType = InputType.EDIT_TEXT,
        title = "Measure and check the temperature. Action – Give the patient paracetamol tablet if the temperature is 102°F (38.9°C) and refer to the hospital if the temperature is higher than this.",
        etInputType = android.text.InputType.TYPE_CLASS_NUMBER or android.text.InputType.TYPE_NUMBER_VARIATION_NORMAL,
        required = false
    )
    val motherWaterDischarge = FormInputOld(
        inputType = InputType.RADIO,
        title = "Water discharge with foul smell and fever 102 degree Fahrenheit (38.9 degree C). ",
        entries = arrayOf("Yes", "No"),
        required = false,
    )
    val motherSpeakAbnormalFits = FormInputOld(
        inputType = InputType.RADIO,
        title = "Is mother speaking abnormally or having fits?",
        entries = arrayOf("Yes", "No"),
        required = false,
    )
    val motherNoOrLessMilk = FormInputOld(
        inputType = InputType.RADIO,
        title = "Mothers milk is not being produced after delivery or she thinks less milk is being produced.",
        entries = arrayOf("Yes", "No"),
        required = false,
    )
    val motherBreastProblem = FormInputOld(
        inputType = InputType.RADIO,
        title = "Does the mother have cracked nipple / pain and / or hard breasts",
        entries = arrayOf("Yes", "No"),
        required = false,
    )

    //////////////////// Part - C //////////////////

    private val titleHealthCheckUpBabyC = FormInputOld(
        inputType = InputType.HEADLINE,
        title = "(c) Health check-up of newborn baby ",
        required = false
    )
    val babyEyesSwollen = FormInputOld(
        inputType = InputType.RADIO,
        title = "Are the eyes swollen / Are there pus from the eyes?",
        entries = arrayOf("Yes", "No"),
        required = false,
    )
    val babyWeight = FormInputOld(
        inputType = InputType.EDIT_TEXT,
        etInputType = android.text.InputType.TYPE_CLASS_NUMBER or android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL,
        minDecimal = 0.5,
        maxDecimal = 7.0,
        etMaxLength = 3,
        title = "Weight on Day $nthDay",
        required = false,
    )

    private val babyBodyTemperature2 = FormInputOld(
        inputType = InputType.EDIT_TEXT,
        title = "Measure and enter temperature",
        etInputType = android.text.InputType.TYPE_CLASS_NUMBER or android.text.InputType.TYPE_NUMBER_VARIATION_NORMAL,
        required = false
    )


    private val pusPimples = FormInputOld(
        inputType = InputType.RADIO,
        title = "Pus filled pimples in the skin",
        entries = arrayOf("Yes", "No"),
        required = false,
    )
    private val crackRedTwistSkin = FormInputOld(
        inputType = InputType.EDIT_TEXT,
        title = "Cracked / redness of twisted skin (thigh / armpit / hip / buttock) - Write",
        required = false
    )
    private val yellowJaundice = FormInputOld(
        inputType = InputType.RADIO,
        title = "Yellowing of the eye/palm/sole/skin (jaundice)",
        entries = arrayOf("Yes", "No"),
        required = false
    )

    private val seizures = FormInputOld(
        inputType = InputType.RADIO,
        title = "Seizures",
        entries = arrayOf("Yes", "No"),
        required = false,
    )
    private val breathFast = FormInputOld(
        inputType = InputType.RADIO,
        title = "Respiratory rate more than 60 per minute",
        entries = arrayOf("Yes", "No"),
        required = false,
    )
    private val referredByAsha = FormInputOld(
        inputType = InputType.EDIT_TEXT,
        title = "In the above symptoms whether the child is referred by ASHA, if yes, then where (HSC/PHC/RH/SDH/DH) ",
        required = false
    )

    ////////////////////////// Part D ////////////////////

    private val titleSepsisD = FormInputOld(
        inputType = InputType.HEADLINE,
        title = "(D) Sepsis",
        subtitle = "Examine the following symptoms of sepsis. If symptoms are present, then write, Yes, if symptoms are not present, then do not write. Enter the symptoms seen from the health check-up on the first day of the newborns birth.",
        required = false
    )
    private val organLethargic = FormInputOld(
        inputType = InputType.EDIT_TEXT, title = "All organs are lethargic", required = false
    )
    private val drinkLessNoMilk = FormInputOld(
        inputType = InputType.EDIT_TEXT,
        title = "Is drinking less milk/has stopped drinking milk ",
        required = false
    )
    private val slowNoCry = FormInputOld(
        inputType = InputType.EDIT_TEXT, title = "Cry weak/ stopped", required = false
    )
    private val bloatedStomach = FormInputOld(
        inputType = InputType.RADIO,
        title = "Distended abdomen or mother says baby vomits often",
        entries = arrayOf(
            "Yes",
            "No",
        ),
        required = false
    )
    private val childColdOnTouch = FormInputOld(
        inputType = InputType.RADIO,
        title = "The mother tells that the child feels cold when touching or the temperature of the child is more than 89 degrees Fahrenheit (37.5 degrees C)",
        entries = arrayOf(
            "Yes",
            "No",
        ),
        required = false
    )

    private val childChestDrawing = FormInputOld(
        inputType = InputType.RADIO,
        title = "and the chest is pulled inward while breathing.",
        entries = arrayOf(
            "Yes",
            "No",
        ),
        required = false
    )


    private val pusNavel = FormInputOld(
        inputType = InputType.EDIT_TEXT, title = "Pus in the navel", required = false
    )
    private val ashaName = FormInputOld(
        inputType = InputType.TEXT_VIEW, title = "ASHA NAME", required = false
    )
    private val villageName = FormInputOld(
        inputType = InputType.TEXT_VIEW, title = "Village Name", required = false
    )
    private val blockName = FormInputOld(
        inputType = InputType.TEXT_VIEW, title = "Block Name", required = false
    )
    private val stillBirth = FormInputOld(
        inputType = InputType.RADIO, title = "Still Birth", entries = arrayOf(
            "Yes",
            "No",
        ), required = false
    )
    private val supRemark = FormInputOld(
        inputType = InputType.EDIT_TEXT, title = "Supervisors Remark ", required = false
    )
    private val sup = FormInputOld(
        inputType = InputType.DROPDOWN, title = "Supervisor", entries = arrayOf(
            "ASHA Facilitator",
            "ANM",
            "MPW",
        ), required = false
    )
    private val supName = FormInputOld(
        inputType = InputType.EDIT_TEXT, title = "Supervisor name", required = false
    )
    private val supervisorComment = FormInputOld(
        inputType = InputType.DROPDOWN,
        title = "Supervisors Comments: Please Tick Mark",
        entries = arrayOf(
            "Fully filled format ",
            "Incomplete format ",
            "Wrongly filled format",
        ),
        required = false,
    )
    private val dateOfSupSig = FormInputOld(
        inputType = InputType.DATE_PICKER,
        title = "Signature with Date of Supervisor",
        min = 0L,
        max = System.currentTimeMillis(),
        required = false
    )

    private val titleVisitCard = FormInputOld(
        inputType = InputType.HEADLINE, title = "Mother-Newborn Home Visit Card", required = false
    )
    private val titleVisitCardDischarge = FormInputOld(
        inputType = InputType.HEADLINE,
        title = "Discharge of Institutional Delivery",
        required = false
    )

    private val titleDateOfHomeVisit = FormInputOld(
        inputType = InputType.HEADLINE, title = "Date of Home Visit", required = false
    )
    val babyAlive = FormInputOld(
        inputType = InputType.RADIO, title = "Is the baby alive?", entries = arrayOf(
            "Yes",
            "No",
        ), required = false
    )
    val dateOfBabyDeath = FormInputOld(
        inputType = InputType.DATE_PICKER,
        title = "Date of death of baby",
        min = 0L,
        max = System.currentTimeMillis(),
        required = false
    )
    val timeOfBabyDeath = FormInputOld(
        inputType = InputType.TIME_PICKER, title = "Time of death of baby", required = false
    )
    val placeOfBabyDeath = FormInputOld(
        inputType = InputType.DROPDOWN,
        title = "Place of Baby Death",
        entries = arrayOf(
            "Home",
            "Sub-center",
            "PHC",
            "CHC",
            "Other",
        ),
        required = false,
    )
    val otherPlaceOfBabyDeath = FormInputOld(
        inputType = InputType.EDIT_TEXT, title = "Other place of Baby Death", required = false
    )
    val babyPreterm = FormInputOld(
        inputType = InputType.RADIO,
        title = "Is the baby preterm?",
        entries = arrayOf(
            "Yes",
            "No",
        ),
        required = false,
    )
    val gestationalAge = FormInputOld(
        inputType = InputType.RADIO,
        title = "How many weeks has it been since baby born (Gestational Age)",
//        orientation = LinearLayout.VERTICAL,
        entries = arrayOf(
            "24 – 34 Weeks",
            "34 – 36 Weeks",
            "36 – 38 Weeks",
        ),
        required = false,
    )
    private val dateOfBabyFirstExamination = FormInputOld(
        inputType = InputType.DATE_PICKER,
        title = "Date of First examination of baby",
        min = 0L,
        max = System.currentTimeMillis(),
        required = false
    )
    private val timeOfBabyFirstExamination = FormInputOld(
        inputType = InputType.TIME_PICKER,
        title = "Time of First examination of baby",
        required = false
    )


    val motherAlive = FormInputOld(
        inputType = InputType.RADIO, title = "Is the mother alive?", entries = arrayOf(
            "Yes",
            "No",
        ), required = false
    )
    val dateOfMotherDeath = FormInputOld(
        inputType = InputType.DATE_PICKER,
        title = "Date of death of mother",
        min = 0L,
        max = System.currentTimeMillis(),
        required = false
    )
    val timeOfMotherDeath = FormInputOld(
        inputType = InputType.TIME_PICKER, title = "Time of death of mother", required = false
    )
    val placeOfMotherDeath = FormInputOld(
        inputType = InputType.DROPDOWN,
        title = "Place of mother Death",
        entries = arrayOf(
            "Home",
            "Sub-center",
            "PHC",
            "CHC",
            "Other",
        ),
        required = false,
    )
    val otherPlaceOfMotherDeath = FormInputOld(
        inputType = InputType.EDIT_TEXT, title = "Other place of mother Death", required = false
    )
    val motherProblems = FormInputOld(
        inputType = InputType.CHECKBOXES,
        title = "Does Mother have any problems",
        entries = arrayOf(
            "Excessive Bleeding",
            "Unconscious / Fits",
        ),
        required = false
    )

    val otherBabyFedAfterBirth = FormInputOld(
        inputType = InputType.EDIT_TEXT,
        title = "Other - What was given as the first feed to baby after birth?",
        required = false
    )
    private val whenBabyFirstFed = FormInputOld(
        inputType = InputType.TIME_PICKER,
        title = "When was the baby first fed",
        required = false
    )
    val motherHasBreastFeedProblem = FormInputOld(
        inputType = InputType.RADIO,
        title = "Does the mother have breastfeeding problem?",
        entries = arrayOf("Yes", "No"),
        required = false,
    )
    val motherBreastFeedProblem = FormInputOld(
        inputType = InputType.EDIT_TEXT,
        title = "Write the problem, if there is any problem in breast feeding, help the mother to overcome it",
        required = false
    )


    ///////////////////////////Part II////////////////////////////
    private val titleBabyFirstHealthCheckup = FormInputOld(
        inputType = InputType.HEADLINE,
        title = "Part 2: Baby first health check-up",
        required = false
    )
    private val babyTemperature = FormInputOld(
        inputType = InputType.EDIT_TEXT, title = "Temperature of the baby", required = false
    )

    private val actionUmbilicalBleed = FormInputOld(
        inputType = InputType.RADIO,
        title = "If yes, either ASHA, ANM/MPW or TBA can tie again with a clean thread. Action taken? ",
        entries = arrayOf(
            "Yes",
            "No",
        ),
        required = false
    )
    private val babyWeigntMatchesColor = FormInputOld(
        inputType = InputType.RADIO, title = "Weighing matches with the colour?", entries = arrayOf(
            "Yes",
            "No",
        ), required = false
    )
    private val titleRoutineNewBornCare = FormInputOld(
        inputType = InputType.HEADLINE,
        title = "Routine Newborn Care: whether the task was performed",
        required = false
    )
    private val babyDry = FormInputOld(
        inputType = InputType.RADIO, title = "Dry the baby", entries = arrayOf(
            "Yes",
            "No",
        ), required = false
    )
    private val cryWeakStopped = FormInputOld(
        inputType = InputType.RADIO, title = "Cry weak/ stopped", entries = arrayOf(
            "Yes",
            "No",
        ), required = false
    )
    private val cordCleanDry = FormInputOld(
        inputType = InputType.RADIO, title = "Keep the cord clean and dry", entries = arrayOf(
            "Yes",
            "No",
        ), required = false
    )

    val unusualWithBaby = FormInputOld(
        inputType = InputType.RADIO,
        title = "Was there anything unusual with the baby?",
        entries = arrayOf("Curved limbs", "cleft lip", "Other"),
        required = false,
    )
    val otherUnusualWithBaby = FormInputOld(
        inputType = InputType.EDIT_TEXT, title = "Other - unusual with the baby", required = false
    )

    /////////////// Part Visit //////////

    private val titleWashHands = FormInputOld(
        inputType = InputType.HEADLINE,
        title = "ASHA should wash hands with soap and water before touching the baby during each visit",
        required = false
    )
    val babyReferred = FormInputOld(
        inputType = InputType.RADIO,
        title = "Baby referred for any reason?",
        entries = arrayOf("Yes", "No"),
        required = false
    )
    val dateOfBabyReferral = FormInputOld(
        inputType = InputType.DATE_PICKER,
        title = "Date of baby referral",
        min = 0L,
        max = System.currentTimeMillis(),
        required = false
    )
    val placeOfBabyReferral = FormInputOld(
        inputType = InputType.DROPDOWN, title = "Place of baby referral", entries = arrayOf(
            "Sub-Centre",
            "PHC",
            "CHC",
            "Sub-District Hospital",
            "District Hospital",
            "Medical College Hospital",
            "In Transit",
            "Private Hospital",
            "Accredited Private Hospital",
            "Other",
        ), required = false
    )
    val otherPlaceOfBabyReferral = FormInputOld(
        inputType = InputType.EDIT_TEXT, title = "Other -Place of baby referral", required = false
    )
    val motherReferred = FormInputOld(
        inputType = InputType.RADIO,
        title = "Mother referred for any reason?",
        entries = arrayOf("Yes", "No"),
        required = false
    )
    val dateOfMotherReferral = FormInputOld(
        inputType = InputType.DATE_PICKER,
        title = "Date of mother referral",
        min = 0L,
        max = System.currentTimeMillis(),
        required = false
    )
    val placeOfMotherReferral = FormInputOld(
        inputType = InputType.DROPDOWN, title = "Place of mother referral", entries = arrayOf(
            "Sub-Centre",
            "PHC",
            "CHC",
            "Sub-District Hospital",
            "District Hospital",
            "Medical College Hospital",
            "In Transit",
            "Private Hospital",
            "Accredited Private Hospital",
            "Other",
        ), required = false
    )
    val otherPlaceOfMotherReferral = FormInputOld(
        inputType = InputType.EDIT_TEXT, title = "Other -Place of mother referral", required = false
    )


    private val cardPage by lazy {
        listOf(
            titleVisitCard,
            ashaName,
            villageName,
            healthSubCenterName,
            blockName,
            motherName,
            fatherName,
            dateOfDelivery,
            placeOfDelivery,
            gender,
            typeOfDelivery,
            stillBirth,
            startedBreastFeeding,
            titleVisitCardDischarge,
            dateOfDischargeFromHospitalMother,
            dateOfDischargeFromHospitalBaby,
            weightAtBirth,
            registrationOfBirth
        )
    }

    fun getCardPage(
        asha: UserCache, childBen: BenRegCache, motherBen: BenRegCache?
        , visitCard: HbncVisitCard?, exists: Boolean
    ): List<FormInputOld> {
        ashaName.value.value = asha.userName
//        villageName.value.value = asha.villageEnglish[0]
//        blockName.value.value = asha.blockEnglish[0]
        motherName.value.value = childBen.motherName
        fatherName.value.value = childBen.fatherName
        placeOfDelivery.value.value = childBen.kidDetails?.birthPlace
        gender.value.value = gender.entries?.get(childBen.genderId)
        typeOfDelivery.value.value =
            childBen.kidDetails?.deliveryTypeId?.let { typeOfDelivery.entries?.get(it) }
        motherBen?.let {
            dateOfDelivery.value.value = it.genDetails?.deliveryDate
        }
         if(exists)
             setExistingValuesForCardPage(visitCard)

        return cardPage


    }

    fun setExistingValuesForCardPage(visitCard: HbncVisitCard?) {
        visitCard?.let {
            healthSubCenterName.value.value = it.subCenterName
            dateOfDelivery.value.value = getDateFromLong(it.dateOfDelivery)
            gender.value.value = gender.entries?.get(it.babyGender)
            stillBirth.value.value = stillBirth.getStringFromPosition(it.stillBirth)
            startedBreastFeeding.value.value =
                startedBreastFeeding.getStringFromPosition(it.startedBreastFeeding)
            dateOfDischargeFromHospitalMother.value.value = getDateFromLong(it.dischargeDateMother)
            dateOfDischargeFromHospitalBaby.value.value = getDateFromLong(it.dischargeDateMother)
            weightAtBirth.value.value = it.weightInGrams.toString()
            registrationOfBirth.value.value = registrationOfBirth.getStringFromPosition(it.registrationOfBirth)

        }

    }



    private val partIPage by lazy {
        listOf(
            titleDateOfHomeVisit,
            babyAlive,
            babyPreterm,
            dateOfBabyFirstExamination,
            timeOfBabyFirstExamination,
            motherAlive,
            motherProblems,
            babyFedAfterBirth,
            whenBabyFirstFed,
            howBabyTookFirstFeed,
            motherHasBreastFeedProblem,
        )
    }

    fun getPartIPage(visitCard: HbncVisitCard?, hbncPartI: HbncPartI?, exists: Boolean): List<FormInputOld> {
        babyAlive.value.value = visitCard?.stillBirth?.let {
           when(it){
               0 -> null
               1 -> babyAlive.entries?.get(1)
               2 -> babyAlive.entries?.get(0)
               else -> null
           } }
        return if(!exists)
            partIPage
        else{
            setExistingValuesForPartIPage(hbncPartI)
            val list = partIPage.toMutableList()
            addNecessaryDependantFieldsToList(list, hbncPartI)
            list
        }
    }

    private fun addNecessaryDependantFieldsToList(list: MutableList<FormInputOld>, hbncPartI: HbncPartI?) {
        hbncPartI?.let {
            if(it.babyAlive==2) {
                list.addAll(
                    list.indexOf(babyAlive) + 1,
                    listOf(
                        dateOfBabyDeath,
                        timeOfBabyDeath,
                        placeOfBabyDeath,
                    )
                )
                if(it.placeOfBabyDeath == (placeOfBabyDeath.entries!!.size-1))
                    list.add(list.indexOf(placeOfBabyDeath)+1,otherPlaceOfBabyDeath)

            }
            if(it.motherAlive==2) {
                list.addAll(
                    list.indexOf(motherAlive) + 1,
                    listOf(
                        dateOfMotherDeath,
                        timeOfMotherDeath,
                        placeOfMotherDeath
                    )
                )
                if(it.placeOfMotherDeath == (placeOfMotherDeath.entries!!.size-1))
                    list.add(list.indexOf(placeOfMotherDeath)+1,otherPlaceOfMotherDeath)
            }
        }

    }

    private fun setExistingValuesForPartIPage(hbncPartI : HbncPartI?) {
        hbncPartI?.let {
            babyAlive.value.value = babyAlive.getStringFromPosition(it.babyAlive)
            dateOfBabyDeath.value.value = getDateFromLong(it.dateOfBabyDeath)
            timeOfBabyDeath.value.value = it.timeOfBabyDeath
            placeOfBabyDeath.value.value = placeOfBabyDeath.getStringFromPosition(it.placeOfBabyDeath)
            otherPlaceOfBabyDeath.value.value = it.otherPlaceOfBabyDeath
            dateOfMotherDeath.value.value = getDateFromLong(it.dateOfMotherDeath)
            timeOfMotherDeath.value.value = it.timeOfMotherDeath
            placeOfMotherDeath.value.value = placeOfMotherDeath.getStringFromPosition(it.placeOfMotherDeath)
            otherPlaceOfMotherDeath.value.value = it.otherPlaceOfMotherDeath
            babyPreterm.value.value = babyPreterm.getStringFromPosition(it.isBabyPreterm)
            dateOfBabyFirstExamination.value.value = getDateFromLong(it.dateOfFirstExamination)
            timeOfBabyFirstExamination.value.value = it.timeOfFirstExamination
            motherAlive.value.value = motherAlive.getStringFromPosition(it.motherAlive)
            motherProblems.value.value = it.motherAnyProblem
            babyFedAfterBirth.value.value = babyFedAfterBirth.getStringFromPosition(it.babyFirstFed)
            whenBabyFirstFed.value.value = it.timeBabyFirstFed
            howBabyTookFirstFeed.value.value = howBabyTookFirstFeed.getStringFromPosition(it.howBabyTookFirstFeed)
            motherHasBreastFeedProblem.value.value = motherHasBreastFeedProblem.getStringFromPosition(it.motherHasBreastFeedProblem)
            motherBreastProblem.value.value = it.motherBreastFeedProblem
        }

    }


    private val partIIPage by lazy {
        listOf(
            titleBabyFirstHealthCheckup,
            babyTemperature,
            babyEyeCondition,
            babyBleedUmbilicalCord,
            actionUmbilicalBleed,
            babyWeight,
            babyWeigntMatchesColor,
            babyWeightColor,
            titleBabyPhysicalCondition,
            allLimbsLimp,
            feedingLessStop,
            cryWeakStopped,
            titleRoutineNewBornCare,
            babyDry,
            wrapClothKeptMother,
            onlyBreastMilk,
            cordCleanDry,
            unusualWithBaby
        )
    }

    suspend fun getPartIIPage(): List<FormInputOld> {
        return partIIPage
    }
    fun setExistingValuesForPartIIPage(hbnc: HBNCCache) {
        hbnc.part2?.let {
            babyTemperature.value.value = it.babyTemperature
            babyEyeCondition.value.value = babyEyeCondition.getStringFromPosition(it.babyEyeCondition)
            babyBleedUmbilicalCord.value.value = babyBleedUmbilicalCord.getStringFromPosition(it.babyUmbilicalBleed)
            actionUmbilicalBleed.value.value = actionUmbilicalBleed.getStringFromPosition(it.actionBabyUmbilicalBleed)
            babyWeight.value.value = it.babyWeight
            babyWeigntMatchesColor.value.value = babyWeigntMatchesColor.getStringFromPosition(it.babyWeightMatchesColor)
            babyWeightColor.value.value = babyWeightColor.getStringFromPosition(it.babyWeightColorOnScale)
            allLimbsLimp.value.value = allLimbsLimp.getStringFromPosition(it.allLimbsLimp)
            feedingLessStop.value.value = feedingLessStop.getStringFromPosition(it.feedLessStop)
            cryWeakStopped.value.value = cryWeakStopped.getStringFromPosition(it.cryWeakStop)
            babyDry.value.value = babyDry.getStringFromPosition(it.dryBaby)
            wrapClothKeptMother.value.value = wrapClothKeptMother.getStringFromPosition(it.wrapClothCloseToMother)
            onlyBreastMilk.value.value = onlyBreastMilk.getStringFromPosition(it.exclusiveBreastFeeding)
            cordCleanDry.value.value = cordCleanDry.getStringFromPosition(it.cordCleanDry)
            unusualWithBaby.value.value = unusualWithBaby.getStringFromPosition(it.unusualInBaby)
        }

    }

    private val visitPage by lazy {
        listOf(
            dateOfAshaVisit,
            titleAskMotherA,
            babyAlive,
            timesMotherFed24hr,
            timesPadChanged,
            babyKeptWarmWinter,
            babyBreastFedProperly,
            babyCryContinuously,
            motherBodyTemperature,
            motherWaterDischarge,
            motherSpeakAbnormalFits,
            motherNoOrLessMilk,
            motherBreastProblem,

            titleWashHands,
            babyEyesSwollen,
            babyWeight,
            babyTemperature,
            yellowJaundice,
            childImmunizationStatus,
            babyReferred,
            motherReferred,
            titleSepsisD,
            allLimbsLimp,
            feedingLessStop,
            cryWeakStopped,
            bloatedStomach,
            childColdOnTouch,
            childChestDrawing,
            breathFast,
            pusNavel,
            sup,
            supName,
            supRemark,
            dateOfSupSig,
        )
    }

    fun getVisitPage(firstDay: HbncHomeVisit?): List<FormInputOld> {
        firstDay?.let {
            childImmunizationStatus.value.value = it.babyImmunizationStatus
        }
        return visitPage
    }

    fun setExistingValuesForVisitPage(hbnc: HBNCCache) {
        hbnc.homeVisitForm?.let {
            dateOfAshaVisit.value.value = getDateFromLong(it.dateOfVisit)
            babyAlive.value.value = babyAlive.getStringFromPosition(it.babyAlive)
            timesMotherFed24hr.value.value = it.numTimesFullMeal24hr.toString()
            timesPadChanged.value.value = it.numPadChanged24hr.toString()
            babyKeptWarmWinter.value.value = babyKeptWarmWinter.getStringFromPosition(it.babyKeptWarmWinter)
            babyBreastFedProperly.value.value = babyBreastFedProperly.getStringFromPosition(it.babyFedProperly)
            babyCryContinuously.value.value = babyCryContinuously.getStringFromPosition(it.babyCryContinuously)
            motherBodyTemperature.value.value = it.motherTemperature
            motherWaterDischarge.value.value = motherWaterDischarge.getStringFromPosition(it.foulDischargeFever)
            motherSpeakAbnormalFits.value.value = motherSpeakAbnormalFits.getStringFromPosition(it.motherSpeakAbnormallyFits)
            motherNoOrLessMilk.value.value = motherNoOrLessMilk.getStringFromPosition(it.motherLessNoMilk)
            motherBreastProblem.value.value = motherBreastProblem.getStringFromPosition(it.motherBreastProblem)
            babyEyesSwollen.value.value = babyEyesSwollen.getStringFromPosition(it.babyEyesSwollen)
            babyWeight.value.value = it.babyWeight
            babyTemperature.value.value = it.babyTemperature
            yellowJaundice.value.value = yellowJaundice.getStringFromPosition(it.babyYellow)
            childImmunizationStatus.value.value = it.babyImmunizationStatus
            babyReferred.value.value = babyReferred.getStringFromPosition(it.babyReferred)
            motherReferred.value.value = motherReferred.getStringFromPosition(it.motherReferred)
            allLimbsLimp.value.value = allLimbsLimp.getStringFromPosition(it.allLimbsLimp)
            feedingLessStop.value.value = feedingLessStop.getStringFromPosition(it.feedingLessStopped)
            cryWeakStopped.value.value = cryWeakStopped.getStringFromPosition(it.cryWeakStopped)
            bloatedStomach.value.value = bloatedStomach.getStringFromPosition(it.bloatedStomach)
            childColdOnTouch.value.value = childColdOnTouch.getStringFromPosition(it.coldOnTouch)
            childChestDrawing.value.value = childChestDrawing.getStringFromPosition(it.chestDrawing)
            breathFast.value.value = breathFast.getStringFromPosition(it.breathFast)
            pusNavel.value.value = pusNavel.getStringFromPosition(it.pusNavel)
            sup.value.value = sup.getStringFromPosition(it.sup)
            supName.value.value = it.supName
            supRemark.value.value = it.supComment
            dateOfSupSig.value.value= getDateFromLong(it.supSignDate)
        }

    }

}