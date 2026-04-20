package com.shrivatsav.quickfix.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.Countertops
import androidx.compose.material.icons.filled.DeviceThermostat
import androidx.compose.material.icons.filled.ElectricalServices
import androidx.compose.material.icons.filled.Handyman
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Opacity
import androidx.compose.material.icons.filled.Park
import androidx.compose.material.icons.filled.PestControl
import androidx.compose.material.icons.filled.Plumbing
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shower
import androidx.compose.material.icons.filled.SolarPower
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material.icons.filled.Waves
import androidx.compose.ui.graphics.vector.ImageVector

// ── Models ────────────────────────────────────────────────────────────────────

data class SubService(
    val id: String,
    val name: String,
    val description: String,
    val icon: ImageVector,
    val estimatedPriceMin: Int,   // in ₹
    val estimatedPriceMax: Int,
    val estimatedDuration: String,  // e.g. "1–2 hrs"
)

data class ServiceCategory(
    val id: String,
    val name: String,
    val description: String,
    val icon: ImageVector,
    val subServices: List<SubService>,
)

// ── Data ──────────────────────────────────────────────────────────────────────

val serviceCategories: List<ServiceCategory> = listOf(

    ServiceCategory(
        id = "plumbing",
        name = "Plumbing",
        description = "Leaks, pipes, drains and water supply",
        icon = Icons.Filled.Plumbing,
        subServices = listOf(
            SubService("plumbing_leak", "Leak Repair", "Fix dripping taps, leaking pipes or joints.", Icons.Filled.Opacity, 300, 600, "1–2 hrs"),
            SubService("plumbing_pipe", "Pipe Installation", "Run new water lines or replace old plumbing.", Icons.Filled.Build, 800, 2000, "2–4 hrs"),
            SubService("plumbing_drain", "Drain Cleaning", "Clear stubborn clogs, blockages and slow drains.", Icons.Filled.Waves, 350, 700, "1–2 hrs"),
            SubService("plumbing_heater", "Water Heater Repair", "Diagnose and fix issues with hot water supply.", Icons.Filled.DeviceThermostat, 500, 1200, "1–3 hrs"),
            SubService("plumbing_shower", "Shower / Tap Fitting", "Install or replace showers, taps and mixers.", Icons.Filled.Shower, 400, 900, "1–2 hrs"),
            SubService("plumbing_toilet", "Toilet Repair", "Fix flushing, leaks, or replace the unit.", Icons.Filled.Home, 300, 700, "1–2 hrs"),
        ),
    ),

    ServiceCategory(
        id = "electrical",
        name = "Electrical",
        description = "Wiring, fittings, fans and safety checks",
        icon = Icons.Filled.Bolt,
        subServices = listOf(
            SubService("elec_fan", "Fan Installation / Repair", "Ceiling or exhaust fan fitting and fault diagnosis.", Icons.Filled.Bolt, 250, 500, "30–60 min"),
            SubService("elec_wiring", "Wiring & Switchboard", "New wiring, switchboard replacement, circuit faults.", Icons.Filled.ElectricalServices, 600, 2500, "2–5 hrs"),
            SubService("elec_mcb", "MCB / Fuse Box", "Trip reset, MCB replacement, load balancing.", Icons.Filled.Security, 400, 1000, "1–2 hrs"),
            SubService("elec_light", "Light / Fixture Fitting", "Install pendant, LED strips, or spot lights.", Icons.Filled.Bolt, 200, 600, "30–90 min"),
            SubService("elec_ac_power", "AC Power Point", "Dedicated 16A socket for air conditioners.", Icons.Filled.ElectricalServices, 350, 700, "1 hr"),
            SubService("elec_solar", "Solar Panel Check", "Inverter diagnostics and panel inspection.", Icons.Filled.SolarPower, 800, 2000, "2–4 hrs"),
        ),
    ),

    ServiceCategory(
        id = "ac_appliance",
        name = "AC & Appliances",
        description = "Air conditioners, geysers and home appliances",
        icon = Icons.Filled.AcUnit,
        subServices = listOf(
            SubService("ac_service", "AC Service / Clean", "Deep clean, gas top-up and filter wash.", Icons.Filled.AcUnit, 499, 999, "1–2 hrs"),
            SubService("ac_install", "AC Installation", "Split or window AC wall mounting and piping.", Icons.Filled.AcUnit, 800, 1500, "2–3 hrs"),
            SubService("ac_repair", "AC Repair", "Cooling issues, compressor faults, remote problems.", Icons.Filled.AcUnit, 500, 2000, "1–3 hrs"),
            SubService("appl_washing", "Washing Machine Repair", "Error codes, drum issues, drain pump faults.", Icons.Filled.Countertops, 400, 1200, "1–2 hrs"),
            SubService("appl_fridge", "Refrigerator Repair", "Cooling loss, ice build-up, compressor check.", Icons.Filled.DeviceThermostat, 500, 1500, "1–3 hrs"),
            SubService("appl_microwave", "Microwave / OTG Repair", "Heating faults, door seal, turntable issues.", Icons.Filled.LocalFireDepartment, 300, 800, "1–2 hrs"),
        ),
    ),

    ServiceCategory(
        id = "cleaning",
        name = "Cleaning",
        description = "Home, sofa, bathroom and kitchen deep clean",
        icon = Icons.Filled.CleaningServices,
        subServices = listOf(
            SubService("clean_home", "Full Home Cleaning", "Complete sweep, mop, surface wipe for all rooms.", Icons.Filled.CleaningServices, 999, 2500, "3–6 hrs"),
            SubService("clean_bathroom", "Bathroom Deep Clean", "Tiles, fixtures, drain and anti-bacterial treatment.", Icons.Filled.Shower, 499, 999, "1–2 hrs"),
            SubService("clean_kitchen", "Kitchen Deep Clean", "Chimney, stove, counters, cabinets and sink.", Icons.Filled.Countertops, 699, 1499, "2–4 hrs"),
            SubService("clean_sofa", "Sofa / Carpet Cleaning", "Foam extraction shampoo for upholstery and rugs.", Icons.Filled.Home, 499, 1200, "1–3 hrs"),
            SubService("clean_tank", "Water Tank Cleaning", "Drain, scrub, disinfect overhead or underground tank.", Icons.Filled.Waves, 800, 2000, "2–4 hrs"),
            SubService("clean_pest", "Pest Control", "Cockroach, ant, termite and bed-bug treatment.", Icons.Filled.PestControl, 499, 1500, "1–3 hrs"),
        ),
    ),

    ServiceCategory(
        id = "carpentry",
        name = "Carpentry",
        description = "Furniture, doors, windows and woodwork",
        icon = Icons.Filled.Handyman,
        subServices = listOf(
            SubService("carp_assemble", "Furniture Assembly", "Flat-pack or modular furniture setup.", Icons.Filled.Handyman, 300, 800, "1–3 hrs"),
            SubService("carp_repair", "Furniture Repair", "Broken hinges, loose joints, drawer fixes.", Icons.Filled.Build, 250, 600, "1–2 hrs"),
            SubService("carp_door", "Door / Window Fix", "Alignment, lock replacement, hinge repair.", Icons.Filled.Home, 300, 700, "1–2 hrs"),
            SubService("carp_wardrobe", "Wardrobe / Cabinet Work", "Custom fitting, shelving or sliding door track.", Icons.Filled.Straighten, 800, 3000, "2–5 hrs"),
            SubService("carp_tv_mount", "TV & Shelf Mounting", "Wall-mount TV, floating shelves or brackets.", Icons.Filled.Handyman, 299, 599, "1–2 hrs"),
        ),
    ),

    ServiceCategory(
        id = "painting",
        name = "Painting",
        description = "Interior, exterior and waterproofing",
        icon = Icons.Filled.Home,
        subServices = listOf(
            SubService("paint_room", "Room Painting", "Primer + 2 coats emulsion for walls and ceiling.", Icons.Filled.Home, 1500, 5000, "1–2 days"),
            SubService("paint_exterior", "Exterior Painting", "Weather-resistant paint for outer walls.", Icons.Filled.Home, 3000, 15000, "2–5 days"),
            SubService("paint_waterproof", "Waterproofing", "Terrace, bathroom or basement leak sealing.", Icons.Filled.Waves, 2000, 8000, "1–3 days"),
            SubService("paint_texture", "Texture / Design Work", "Stencil, texture coat or decorative finish.", Icons.Filled.Home, 1000, 4000, "1–2 days"),
            SubService("paint_touch", "Touch-Up", "Small area patch, peeling or stain cover.", Icons.Filled.Home, 300, 800, "2–4 hrs"),
        ),
    ),

    ServiceCategory(
        id = "gardening",
        name = "Gardening",
        description = "Lawn care, plant care and landscaping",
        icon = Icons.Filled.Park,
        subServices = listOf(
            SubService("garden_lawn", "Lawn Mowing", "Trim, edge and clean up grass.", Icons.Filled.Park, 300, 700, "1–2 hrs"),
            SubService("garden_prune", "Tree / Shrub Pruning", "Shape and prune overgrown plants safely.", Icons.Filled.Park, 400, 1000, "1–3 hrs"),
            SubService("garden_soil", "Soil & Compost", "Repot, top soil or fertiliser application.", Icons.Filled.Park, 250, 600, "1–2 hrs"),
            SubService("garden_design", "Garden Design", "Layout plan and planting for a new garden.", Icons.Filled.Park, 1500, 5000, "half day+"),
        ),
    ),
)