# Technical Specifications & Reference Code - SafeGuard

This document provides a complete technical blueprint of the SafeGuard application. It contains the exact logic, styling rules, HTML layouts, and component structures implemented in the project. Use this reference to recreate the app in **React** or **Android (Kotlin)**.

---

## 🎨 1. Design System & Theme (Maia Aesthetic)

The Maia theme utilizes rounded corners (`--radius: 1rem`), generous padding, and an emerald/slate palette.

### 1.1 CSS Core Configurations (`src/styles.css`)
```css
/* Global Stylesheet */
@import 'tailwindcss';
@import 'leaflet/dist/leaflet.css';

@theme {
  --color-background: var(--background);
  --color-foreground: var(--foreground);
  --color-primary: var(--primary);
  --color-primary-foreground: var(--primary-foreground);
  --color-card: var(--card);
  --color-card-foreground: var(--card-foreground);
  --color-popover: var(--popover);
  --color-popover-foreground: var(--popover-foreground);
  --color-secondary: var(--secondary);
  --color-secondary-foreground: var(--secondary-foreground);
  --color-muted: var(--muted);
  --color-muted-foreground: var(--muted-foreground);
  --color-accent: var(--accent);
  --color-accent-foreground: var(--accent-foreground);
  --color-destructive: var(--destructive);
  --color-destructive-foreground: var(--destructive-foreground);
  --color-border: var(--border);
  --color-input: var(--input);
  --color-ring: var(--ring);

  --radius-lg: var(--radius);
  --radius-md: calc(var(--radius) - 2px);
  --radius-sm: calc(var(--radius) - 4px);
}

:root {
  --radius: 1rem;

  --background: oklch(0.99 0.005 160);
  --foreground: oklch(0.20 0.02 165);

  --card: oklch(1 0 0);
  --card-foreground: oklch(0.20 0.02 165);

  --popover: oklch(1 0 0);
  --popover-foreground: oklch(0.20 0.02 165);

  --primary: oklch(0.58 0.16 160); /* Vibrant Emerald Mint */
  --primary-foreground: oklch(0.99 0.005 160);

  --secondary: oklch(0.94 0.015 165); /* Soft Pale Sage */
  --secondary-foreground: oklch(0.35 0.03 165);

  --muted: oklch(0.96 0.01 165);
  --muted-foreground: oklch(0.50 0.02 165);

  --accent: oklch(0.92 0.03 160);
  --accent-foreground: oklch(0.30 0.05 160);

  --destructive: oklch(0.60 0.18 25); /* Emergency Red */
  --destructive-foreground: oklch(0.98 0.01 25);

  --border: oklch(0.88 0.01 165);
  --input: oklch(0.88 0.01 165);
  --ring: oklch(0.58 0.16 160);
}

.dark {
  --background: oklch(0.14 0.015 165); /* Deep Organic Slate */
  --foreground: oklch(0.95 0.01 160);

  --card: oklch(0.17 0.015 165);
  --card-foreground: oklch(0.95 0.01 160);

  --popover: oklch(0.17 0.015 165);
  --popover-foreground: oklch(0.95 0.01 160);

  --primary: oklch(0.68 0.15 160); /* Bright Emerald Mint */
  --primary-foreground: oklch(0.14 0.015 165);

  --secondary: oklch(0.22 0.015 165);
  --secondary-foreground: oklch(0.85 0.02 160);

  --muted: oklch(0.20 0.015 165);
  --muted-foreground: oklch(0.70 0.015 160);

  --accent: oklch(0.25 0.03 160);
  --accent-foreground: oklch(0.90 0.03 160);

  --destructive: oklch(0.55 0.18 25);
  --destructive-foreground: oklch(0.98 0.01 25);

  --border: oklch(0.25 0.015 165);
  --input: oklch(0.25 0.015 165);
  --ring: oklch(0.68 0.15 160);
}

body {
  background-color: var(--background);
  color: var(--foreground);
  font-family: 'Outfit', 'Inter', system-ui, sans-serif;
  margin: 0;
  padding: 0;
  transition: background-color 0.3s ease, color 0.3s ease;
}

/* Custom Leaflet Styling */
.leaflet-container {
  font-family: inherit;
  background-color: var(--background) !important;
}

.leaflet-bar {
  border: none !important;
  box-shadow: 0 4px 6px -1px rgb(0 0 0 / 0.1), 0 2px 4px -2px rgb(0 0 0 / 0.1) !important;
  border-radius: var(--radius) !important;
  overflow: hidden;
}

.leaflet-bar a {
  background-color: var(--card) !important;
  color: var(--foreground) !important;
  border-bottom: 1px solid var(--border) !important;
  transition: background-color 0.2s;
}

.leaflet-bar a:hover {
  background-color: var(--accent) !important;
  color: var(--accent-foreground) !important;
}

.leaflet-popup-content-wrapper {
  background-color: var(--card) !important;
  color: var(--card-foreground) !important;
  border-radius: var(--radius) !important;
  box-shadow: 0 10px 15px -3px rgb(0 0 0 / 0.1) !important;
  border: 1px solid var(--border);
  padding: 4px;
}

.leaflet-popup-tip {
  background-color: var(--card) !important;
  border: 1px solid var(--border);
}

::-webkit-scrollbar {
  width: 8px;
  height: 8px;
}

::-webkit-scrollbar-track {
  background: transparent;
}

::-webkit-scrollbar-thumb {
  background: var(--border);
  border-radius: 9999px;
}

::-webkit-scrollbar-thumb:hover {
  background: var(--muted-foreground);
}
```

---

## 💾 2. Core Models

### 2.1 Device Model (`src/app/core/models/device.model.ts`)
```typescript
export interface Device {
  id: string;
  name: string;
  type: 'watch' | 'collar' | 'tracker' | 'phone';
  status: 'online' | 'offline' | 'battery-low';
  battery: number;
  latitude: number;
  longitude: number;
  speed: number; // in km/h
  lastActive: Date;
  avatar: string;
}
```

### 2.2 Geofence Model (`src/app/core/models/geofence.model.ts`)
```typescript
export interface Geofence {
  id: string;
  name: string;
  latitude: number;
  longitude: number;
  radius: number; // in meters
  isActive: boolean;
  status: 'safe' | 'breached';
}
```

### 2.3 SOS Contact Model (`src/app/core/models/sos-contact.model.ts`)
```typescript
export interface SOSContact {
  id: string;
  name: string;
  relation: string;
  phone: string;
  email: string;
  isAlertEnabled: boolean;
}
```

---

## ⚙ 3. Core Services & Logic

### 3.1 Permissions Service (`src/app/core/services/permissions.service.ts`)
```typescript
import { Injectable, signal, computed } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class PermissionsService {
  private _geolocationPermission = signal<PermissionState>('prompt');

  public geolocationPermission = computed(() => this._geolocationPermission());

  constructor() {
    if (typeof window !== 'undefined') {
      this.checkPermissions();
    }
  }

  async checkPermissions() {
    if (typeof navigator === 'undefined' || !navigator.permissions) {
      return;
    }

    try {
      const status = await navigator.permissions.query({ name: 'geolocation' as PermissionName });
      this._geolocationPermission.set(status.state);
      status.onchange = () => {
        this._geolocationPermission.set(status.state);
      };
    } catch (e) {
      console.warn('Geolocation permission query not supported:', e);
    }
  }

  async requestGeolocation(): Promise<boolean> {
    return new Promise((resolve) => {
      if (typeof navigator === 'undefined' || !navigator.geolocation) {
        resolve(false);
        return;
      }

      navigator.geolocation.getCurrentPosition(
        () => {
          this._geolocationPermission.set('granted');
          resolve(true);
        },
        (error) => {
          console.warn('Geolocation access denied:', error);
          this._geolocationPermission.set('denied');
          resolve(false);
        }
      );
    });
  }
}
```

### 3.2 Tracking Service (`src/app/core/services/tracking.service.ts`)
```typescript
import { Injectable, signal, computed, OnDestroy } from '@angular/core';
import { interval, Subscription } from 'rxjs';
import { Device } from '../models/device.model';

@Injectable({
  providedIn: 'root',
})
export class TrackingService implements OnDestroy {
  private readonly defaultLat = 37.7749;
  private readonly defaultLng = -122.4194;

  private _devices = signal<Device[]>([
    {
      id: '8C:94:DF:68:ED:40',
      name: 'ESP32 Safety Tracker',
      type: 'tracker',
      status: 'online',
      battery: 100,
      latitude: this.defaultLat,
      longitude: this.defaultLng,
      speed: 0.0,
      lastActive: new Date(),
      avatar: 'https://images.unsplash.com/photo-1533473359331-0135ef1b58bf?auto=format&fit=crop&q=80&w=120'
    }
  ]);

  private _selectedDeviceId = signal<string | null>('8C:94:DF:68:ED:40');
  private _history = signal<Record<string, [number, number][]>>({
    '8C:94:DF:68:ED:40': [[this.defaultLat, this.defaultLng]]
  });

  public devices = computed(() => this._devices());
  public selectedDeviceId = computed(() => this._selectedDeviceId());
  public selectedDevice = computed(() => {
    const id = this._selectedDeviceId();
    return this._devices().find(d => d.id === id) || null;
  });
  public history = computed(() => this._history());

  private simulationSub?: Subscription;

  constructor() {
    this.startSimulation();
  }

  public selectDevice(id: string | null) {
    this._selectedDeviceId.set(id);
  }

  private startSimulation() {
    if (typeof window === 'undefined') return;

    this.simulationSub = interval(5000).subscribe(() => {
      this._devices.update(devices =>
        devices.map(device => {
          const latJitter = (Math.random() - 0.5) * 0.0006;
          const lngJitter = (Math.random() - 0.5) * 0.0006;
          const newLat = device.latitude + latJitter;
          const newLng = device.longitude + lngJitter;

          const speedChange = (Math.random() - 0.5) * 2;
          const newSpeed = Math.max(0, Math.round((device.speed + speedChange) * 10) / 10);

          const newBattery = Math.max(0, device.battery - (Math.random() > 0.9 ? 1 : 0));
          const newStatus = newBattery < 20 ? 'battery-low' : 'online';

          this._history.update(hist => {
            const path = hist[device.id] || [];
            const newPath = [...path, [newLat, newLng] as [number, number]];
            if (newPath.length > 30) newPath.shift();
            return { ...hist, [device.id]: newPath };
          });

          return {
            ...device,
            latitude: newLat,
            longitude: newLng,
            speed: newSpeed,
            battery: newBattery,
            status: newStatus,
            lastActive: new Date()
          };
        })
      );
    });
  }

  ngOnDestroy() {
    this.simulationSub?.unsubscribe();
  }
}
```

### 3.3 Geofencing Service (`src/app/core/services/geofencing.service.ts`)
```typescript
import { Injectable, signal, computed, effect, inject } from '@angular/core';
import { Geofence } from '../models/geofence.model';
import { TrackingService } from './tracking.service';

export interface AlertLog {
  id: string;
  timestamp: Date;
  deviceName: string;
  geofenceName: string;
  type: 'entry' | 'exit'; // exit = breach
  message: string;
}

@Injectable({
  providedIn: 'root',
})
export class GeofencingService {
  private trackingService = inject(TrackingService);

  private _geofences = signal<Geofence[]>([
    {
      id: '1',
      name: 'Home',
      latitude: 37.7749,
      longitude: -122.4194,
      radius: 500, // in meters
      isActive: true,
      status: 'safe',
    },
    {
      id: '2',
      name: 'School',
      latitude: 37.7849,
      longitude: -122.4294,
      radius: 300, // in meters
      isActive: true,
      status: 'safe',
    }
  ]);

  private _logs = signal<AlertLog[]>([
    {
      id: 'log-1',
      timestamp: new Date(Date.now() - 3600000),
      deviceName: 'ESP32 Safety Tracker',
      geofenceName: 'Home',
      type: 'entry',
      message: 'ESP32 Safety Tracker entered Home.',
    }
  ]);

  public geofences = computed(() => this._geofences());
  public logs = computed(() => this._logs());

  public hasBreachedFences = computed(() => {
    return this._geofences().some(f => f.isActive && f.status === 'breached');
  });

  private deviceStateMap = new Map<string, Set<string>>();

  constructor() {
    effect(() => {
      const devices = this.trackingService.devices();
      const fences = this._geofences();

      let stateChanged = false;
      const updatedFences = fences.map(fence => {
        if (!fence.isActive) {
          return { ...fence, status: 'safe' as const };
        }

        let isFenceBreached = false;

        devices.forEach(device => {
          if (device.status === 'offline') return;

          const dist = this.getDistance(
            device.latitude,
            device.longitude,
            fence.latitude,
            fence.longitude
          );

          const wasInside = this.deviceStateMap.get(device.id)?.has(fence.id) ?? false;
          const isInside = dist <= fence.radius;

          if (isInside && !wasInside) {
            if (!this.deviceStateMap.has(device.id)) {
              this.deviceStateMap.set(device.id, new Set());
            }
            this.deviceStateMap.get(device.id)!.add(fence.id);
            this.addLog(device.name, fence.name, 'entry', `${device.name} entered ${fence.name}.`);
          } else if (!isInside && wasInside) {
            this.deviceStateMap.get(device.id)?.delete(fence.id);
            this.addLog(device.name, fence.name, 'exit', `ALERT: ${device.name} exited ${fence.name}!`);
          }

          // Breach conditions for ESP32 Safety Tracker
          if (device.id === '8C:94:DF:68:ED:40' && !isInside) {
            isFenceBreached = true;
          }
        });

        const newStatus = isFenceBreached ? ('breached' as const) : ('safe' as const);
        if (newStatus !== fence.status) {
          stateChanged = true;
          return { ...fence, status: newStatus };
        }
        return fence;
      });

      if (stateChanged) {
        this._geofences.set(updatedFences);
      }
    });
  }

  public addGeofence(fence: Omit<Geofence, 'status'>) {
    this._geofences.update(fences => [...fences, { ...fence, status: 'safe' }]);
    this.addLog('System', fence.name, 'entry', `New geofence ${fence.name} was created.`);
  }

  public toggleGeofence(id: string) {
    this._geofences.update(fences =>
      fences.map(f => (f.id === id ? { ...f, isActive: !f.isActive } : f))
    );
  }

  public deleteGeofence(id: string) {
    const fence = this._geofences().find(f => f.id === id);
    this._geofences.update(fences => fences.filter(f => f.id !== id));
    if (fence) {
      this.addLog('System', fence.name, 'exit', `Geofence ${fence.name} was deleted.`);
    }
  }

  private addLog(deviceName: string, geofenceName: string, type: 'entry' | 'exit', message: string) {
    const newLog: AlertLog = {
      id: Math.random().toString(36).substring(2, 9),
      timestamp: new Date(),
      deviceName,
      geofenceName,
      type,
      message,
    };
    this._logs.update(logs => [newLog, ...logs].slice(0, 50));
  }

  private getDistance(lat1: number, lon1: number, lat2: number, lon2: number): number {
    const R = 6371e3; // Earth radius in meters
    const rad = Math.PI / 180;
    const φ1 = lat1 * rad;
    const φ2 = lat2 * rad;
    const Δφ = (lat2 - lat1) * rad;
    const Δλ = (lon2 - lon1) * rad;

    const a = Math.sin(Δφ / 2) * Math.sin(Δφ / 2) +
              Math.cos(φ1) * Math.cos(φ2) *
              Math.sin(Δλ / 2) * Math.sin(Δλ / 2);
    const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

    return R * c;
  }
}
```

### 3.4 SOS Service (`src/app/core/services/sos.service.ts`)
```typescript
import { Injectable, signal, computed } from '@angular/core';
import { SOSContact } from '../models/sos-contact.model';

@Injectable({
  providedIn: 'root',
})
export class SOSService {
  private _contacts = signal<SOSContact[]>([]);
  private _sosActive = signal<boolean>(false);
  private _sosCoordinates = signal<[number, number] | null>(null);

  public contacts = computed(() => this._contacts());
  public sosActive = computed(() => this._sosActive());
  public sosCoordinates = computed(() => this._sosCoordinates());

  constructor() {
    if (typeof window !== 'undefined') {
      const stored = localStorage.getItem('emergency_contacts');
      if (stored) {
        try {
          const parsed = JSON.parse(stored);
          if (parsed.some((p: any) => p.name === 'Sarah Connor' || p.name === 'John Connor')) {
            this.loadMockContacts();
          } else {
            this._contacts.set(parsed);
          }
        } catch (e) {
          this.loadMockContacts();
        }
      } else {
        this.loadMockContacts();
      }
    }
  }

  private loadMockContacts() {
    const mocks: SOSContact[] = [
      {
        id: '1',
        name: 'Mom',
        relation: 'Priority 1',
        phone: '555-0101',
        email: 'mom@example.com',
        isAlertEnabled: true,
      },
      {
        id: '2',
        name: 'Dad',
        relation: 'Priority 2',
        phone: '555-0102',
        email: 'dad@example.com',
        isAlertEnabled: true,
      }
    ];
    this._contacts.set(mocks);
    this.saveToStorage(mocks);
  }

  private saveToStorage(contacts: SOSContact[]) {
    if (typeof window !== 'undefined') {
      localStorage.setItem('emergency_contacts', JSON.stringify(contacts));
    }
  }

  public addContact(contact: Omit<SOSContact, 'id'>) {
    const newContact: SOSContact = {
      ...contact,
      id: Math.random().toString(36).substring(2, 9),
    };
    this._contacts.update(c => {
      const updated = [...c, newContact];
      this.saveToStorage(updated);
      return updated;
    });
  }

  public updateContact(updatedContact: SOSContact) {
    this._contacts.update(contacts => {
      const updated = contacts.map(c => c.id === updatedContact.id ? updatedContact : c);
      this.saveToStorage(updated);
      return updated;
    });
  }

  public deleteContact(id: string) {
    this._contacts.update(c => {
      const updated = c.filter(contact => contact.id !== id);
      this.saveToStorage(updated);
      return updated;
    });
  }

  public triggerSOS(): Promise<boolean> {
    this._sosActive.set(true);

    return new Promise((resolve) => {
      if (typeof navigator !== 'undefined' && navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(
          (pos) => {
            const coords: [number, number] = [pos.coords.latitude, pos.coords.longitude];
            this._sosCoordinates.set(coords);
            console.log('SOS Active! Broadcasting user coordinates:', coords);
            resolve(true);
          },
          (err) => {
            console.warn('Could not fetch geolocation during SOS, using default.', err);
            this._sosCoordinates.set([37.7749, -122.4194]);
            resolve(true);
          },
          { enableHighAccuracy: true, timeout: 5000 }
        );
      } else {
        this._sosCoordinates.set([37.7749, -122.4194]);
        resolve(true);
      }
    });
  }

  public cancelSOS() {
    this._sosActive.set(false);
    this._sosCoordinates.set(null);
  }
}
```

---

## 🧩 4. Shared UI Components

### 4.1 Button Component (`src/app/shared/ui/button.ts`)
```typescript
import { Directive, Input, HostBinding, signal } from '@angular/core';
import { clsx } from 'clsx';

@Directive({
  selector: '[hlmBtn]',
  standalone: true,
})
export class HlmButtonDirective {
  private _variant = signal<'default' | 'destructive' | 'outline' | 'secondary' | 'ghost' | 'link'>('default');
  private _size = signal<'default' | 'sm' | 'lg' | 'icon'>('default');

  @Input()
  set variant(val: 'default' | 'destructive' | 'outline' | 'secondary' | 'ghost' | 'link') {
    this._variant.set(val);
  }

  @Input()
  set size(val: 'default' | 'sm' | 'lg' | 'icon') {
    this._size.set(val);
  }

  @Input()
  class: string = '';

  @HostBinding('class')
  get classes(): string {
    const base = 'inline-flex items-center justify-center font-medium transition-all duration-200 focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:opacity-50 disabled:pointer-events-none active:scale-[0.98] cursor-pointer';

    const variants = {
      default: 'bg-primary text-primary-foreground hover:bg-primary/90 shadow-sm hover:shadow-md hover:shadow-primary/10',
      destructive: 'bg-destructive text-destructive-foreground hover:bg-destructive/90 shadow-sm hover:shadow-md hover:shadow-destructive/10',
      outline: 'border border-border bg-card text-foreground hover:bg-accent hover:text-accent-foreground',
      secondary: 'bg-secondary text-secondary-foreground hover:bg-secondary/80',
      ghost: 'hover:bg-accent hover:text-accent-foreground',
      link: 'text-primary underline-offset-4 hover:underline',
    };

    const sizes = {
      default: 'h-11 px-6 rounded-lg text-sm',
      sm: 'h-9 px-4 rounded-md text-xs',
      lg: 'h-13 px-8 rounded-xl text-base',
      icon: 'h-11 w-11 rounded-lg text-sm',
    };

    return clsx(base, variants[this._variant()], sizes[this._size()], this.class);
  }
}
```

### 4.2 Card Component (`src/app/shared/ui/card.ts`)
```typescript
import { Directive, HostBinding, Input } from '@angular/core';
import { clsx } from 'clsx';

@Directive({
  selector: '[hlmCard]',
  standalone: true,
})
export class HlmCardDirective {
  @Input() class: string = '';

  @HostBinding('class')
  get classes(): string {
    return clsx(
      'rounded-xl border border-border bg-card text-card-foreground shadow-sm p-6 flex flex-col transition-all duration-300 hover:shadow-md',
      this.class
    );
  }
}

@Directive({
  selector: '[hlmCardHeader]',
  standalone: true,
})
export class HlmCardHeaderDirective {
  @Input() class: string = '';

  @HostBinding('class')
  get classes(): string {
    return clsx('flex flex-col gap-1.5 pb-4', this.class);
  }
}

@Directive({
  selector: '[hlmCardTitle]',
  standalone: true,
})
export class HlmCardTitleDirective {
  @Input() class: string = '';

  @HostBinding('class')
  get classes(): string {
    return clsx('text-xl font-semibold tracking-tight leading-none text-foreground', this.class);
  }
}

@Directive({
  selector: '[hlmCardDescription]',
  standalone: true,
})
export class HlmCardDescriptionDirective {
  @Input() class: string = '';

  @HostBinding('class')
  get classes(): string {
    return clsx('text-sm text-muted-foreground', this.class);
  }
}

@Directive({
  selector: '[hlmCardContent]',
  standalone: true,
})
export class HlmCardContentDirective {
  @Input() class: string = '';

  @HostBinding('class')
  get classes(): string {
    return clsx('text-sm leading-relaxed flex-1', this.class);
  }
}

@Directive({
  selector: '[hlmCardFooter]',
  standalone: true,
})
export class HlmCardFooterDirective {
  @Input() class: string = '';

  @HostBinding('class')
  get classes(): string {
    return clsx('flex items-center pt-4 border-t border-border mt-auto', this.class);
  }
}

export const HlmCardImports = [
  HlmCardDirective,
  HlmCardHeaderDirective,
  HlmCardTitleDirective,
  HlmCardDescriptionDirective,
  HlmCardContentDirective,
  HlmCardFooterDirective
] as const;
```

### 4.3 Slider Component (`src/app/shared/ui/slider.ts`)
```typescript
import { Component, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'hlm-slider',
  standalone: true,
  template: `
    <div class="flex flex-col gap-2 w-full">
      <div class="flex items-center justify-between text-sm">
        <span class="text-muted-foreground font-medium">{{ label }}</span>
        <span class="text-primary font-semibold">{{ value }}{{ unit }}</span>
      </div>
      <div class="relative flex items-center select-none w-full h-5">
        <input
          type="range"
          [min]="min"
          [max]="max"
          [step]="step"
          [value]="value"
          (input)="onInput($event)"
          class="w-full h-2 bg-secondary rounded-lg appearance-none cursor-pointer accent-primary focus:outline-none focus:ring-2 focus:ring-ring focus:ring-offset-2"
        />
      </div>
    </div>
  `,
  styles: [
    `
      input[type='range']::-webkit-slider-runnable-track {
        height: 8px;
        border-radius: 9999px;
      }
      input[type='range']::-webkit-slider-thumb {
        height: 20px;
        width: 20px;
        border-radius: 50%;
        background: var(--primary);
        cursor: pointer;
        appearance: none;
        margin-top: -6px;
        border: 2px solid var(--card);
        box-shadow: 0 4px 6px -1px rgb(0 0 0 / 0.1);
        transition: transform 0.1s;
      }
      input[type='range']::-webkit-slider-thumb:active {
        transform: scale(1.15);
      }
    `
  ]
})
export class HlmSliderComponent {
  @Input() label: string = 'Radius';
  @Input() min: number = 0;
  @Input() max: number = 100;
  @Input() step: number = 1;
  @Input() value: number = 50;
  @Input() unit: string = '';

  @Output() valueChange = new EventEmitter<number>();

  onInput(event: Event) {
    const target = event.target as HTMLInputElement;
    const val = parseFloat(target.value);
    this.value = val;
    this.valueChange.emit(val);
  }
}
```

### 4.4 Dialog Component (`src/app/shared/ui/dialog.ts`)
```typescript
import { Component, Input, Output, EventEmitter, HostListener, signal } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'hlm-dialog',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div *ngIf="isOpen()" class="fixed inset-0 z-50 flex items-center justify-center p-4">
      <div 
        (click)="closeOnBackdropClick()" 
        class="fixed inset-0 bg-background/80 backdrop-blur-sm transition-opacity duration-300"
      ></div>

      <div 
        class="relative z-50 w-full max-w-md rounded-2xl border border-border bg-card p-6 shadow-xl transition-all duration-300 scale-100 opacity-100 flex flex-col gap-4 text-foreground"
        role="dialog"
        aria-modal="true"
      >
        <ng-content></ng-content>
      </div>
    </div>
  `
})
export class HlmDialogComponent {
  public isOpen = signal<boolean>(false);

  @Input() closeOnOutsideClick = true;
  @Output() closed = new EventEmitter<void>();
  @Output() opened = new EventEmitter<void>();

  open() {
    this.isOpen.set(true);
    this.opened.emit();
  }

  close() {
    this.isOpen.set(false);
    this.closed.emit();
  }

  closeOnBackdropClick() {
    if (this.closeOnOutsideClick) {
      this.close();
    }
  }

  @HostListener('document:keydown.escape', ['$event'])
  handleEscape(event: any) {
    if (this.isOpen()) {
      this.close();
    }
  }
}
```

---

## 🏛 5. Layout & Navigation Frame

### 5.1 App Shell (`src/app/layout/app-shell/app-shell.component.ts`)
```typescript
import { Component, inject, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet, RouterLink, RouterLinkActive } from '@angular/router';
import { 
  LucideShield, 
  LucideLayoutDashboard, 
  LucideMapPin, 
  LucideBell, 
  LucidePhone, 
  LucideX, 
  LucideAlertTriangle, 
  LucideSun, 
  LucideMoon 
} from '@lucide/angular';
import { SOSService } from '../../core/services/sos.service';
import { GeofencingService } from '../../core/services/geofencing.service';
import { HlmButtonDirective } from '../../shared/ui/button';
import { HlmDialogComponent } from '../../shared/ui/dialog';

@Component({
  selector: 'app-shell',
  standalone: true,
  imports: [
    CommonModule,
    RouterOutlet,
    RouterLink,
    RouterLinkActive,
    HlmButtonDirective,
    HlmDialogComponent,
    LucideShield,
    LucideLayoutDashboard,
    LucideMapPin,
    LucideBell,
    LucidePhone,
    LucideX,
    LucideAlertTriangle,
    LucideSun,
    LucideMoon
  ],
  template: `
    <div class="flex h-screen w-screen overflow-hidden bg-background font-sans text-foreground">
      <!-- SIDEBAR (Desktop) -->
      <aside 
        class="hidden md:flex flex-col w-64 border-r border-border bg-card p-6 gap-6 transition-all duration-300"
      >
        <div class="flex items-center gap-3">
          <div class="p-2.5 bg-primary/10 rounded-xl text-primary flex items-center justify-center">
            <svg lucideShield class="h-6 w-6 stroke-[2]"></svg>
          </div>
          <div>
            <h1 class="text-lg font-bold tracking-tight">SafeGuard</h1>
            <span class="text-xs text-muted-foreground font-medium">SafeGuard</span>
          </div>
        </div>

        <!-- Navigation Links -->
        <nav class="flex flex-col gap-1.5 flex-1">
          <a 
            routerLink="/dashboard" 
            routerLinkActive="bg-accent text-accent-foreground shadow-sm"
            class="flex items-center gap-3 px-4 py-3 rounded-xl text-sm font-semibold text-muted-foreground hover:bg-accent/50 hover:text-foreground transition-all duration-200"
          >
            <svg lucideLayoutDashboard class="h-5 w-5"></svg>
            Dashboard
          </a>
          <a 
            routerLink="/tracking" 
            routerLinkActive="bg-accent text-accent-foreground shadow-sm"
            class="flex items-center gap-3 px-4 py-3 rounded-xl text-sm font-semibold text-muted-foreground hover:bg-accent/50 hover:text-foreground transition-all duration-200"
          >
            <svg lucideMapPin class="h-5 w-5"></svg>
            Live Tracking
          </a>
          <a 
            routerLink="/geofencing" 
            routerLinkActive="bg-accent text-accent-foreground shadow-sm"
            class="flex items-center gap-3 px-4 py-3 rounded-xl text-sm font-semibold text-muted-foreground hover:bg-accent/50 hover:text-foreground transition-all duration-200"
          >
            <svg lucideBell class="h-5 w-5"></svg>
            Geofencing
          </a>
          <a 
            routerLink="/sos" 
            routerLinkActive="bg-accent text-accent-foreground shadow-sm"
            class="flex items-center gap-3 px-4 py-3 rounded-xl text-sm font-semibold text-muted-foreground hover:bg-accent/50 hover:text-foreground transition-all duration-200"
          >
            <svg lucidePhone class="h-5 w-5"></svg>
            Emergency Contacts
          </a>
        </nav>

        <!-- Footer / Settings -->
        <div class="border-t border-border pt-4 flex flex-col gap-2">
          <div class="flex items-center gap-3 px-2">
            <div class="h-9 w-9 rounded-full bg-secondary flex items-center justify-center font-bold text-sm">A</div>
            <div class="flex-1 min-w-0">
              <p class="text-sm font-semibold truncate">Administrator</p>
              <p class="text-xs text-muted-foreground truncate">admin&#64;safeguard.com</p>
            </div>
          </div>
        </div>
      </aside>

      <!-- MOBILE SIDEBAR / MENU OVERLAY -->
      <div 
        *ngIf="isMobileMenuOpen()" 
        class="fixed inset-0 z-40 bg-background/80 backdrop-blur-sm md:hidden"
        (click)="toggleMobileMenu()"
      ></div>
      <aside 
        class="fixed inset-y-0 left-0 z-50 flex flex-col w-64 border-r border-border bg-card p-6 gap-6 transform transition-transform duration-300 md:hidden"
        [class.translate-x-0]="isMobileMenuOpen()"
        [class.-translate-x-full]="!isMobileMenuOpen()"
      >
        <div class="flex items-center justify-between">
          <div class="flex items-center gap-3">
            <div class="p-2 bg-primary/10 rounded-xl text-primary">
              <svg lucideShield class="h-6 w-6"></svg>
            </div>
            <div>
              <h1 class="text-base font-bold">SafeGuard</h1>
              <span class="text-xs text-muted-foreground">GPS Tracker</span>
            </div>
          </div>
          <button (click)="toggleMobileMenu()" class="p-1.5 hover:bg-secondary rounded-lg">
            <svg lucideX class="h-5 w-5"></svg>
          </button>
        </div>

        <nav class="flex flex-col gap-1.5 flex-1">
          <a 
            routerLink="/dashboard" 
            routerLinkActive="bg-accent text-accent-foreground shadow-sm"
            (click)="toggleMobileMenu()"
            class="flex items-center gap-3 px-4 py-3 rounded-xl text-sm font-semibold text-muted-foreground hover:bg-accent/50 hover:text-foreground transition-all duration-200"
          >
            <svg lucideLayoutDashboard class="h-5 w-5"></svg>
            Dashboard
          </a>
          <a 
            routerLink="/tracking" 
            routerLinkActive="bg-accent text-accent-foreground shadow-sm"
            (click)="toggleMobileMenu()"
            class="flex items-center gap-3 px-4 py-3 rounded-xl text-sm font-semibold text-muted-foreground hover:bg-accent/50 hover:text-foreground transition-all duration-200"
          >
            <svg lucideMapPin class="h-5 w-5"></svg>
            Live Tracking
          </a>
          <a 
            routerLink="/geofencing" 
            routerLinkActive="bg-accent text-accent-foreground shadow-sm"
            (click)="toggleMobileMenu()"
            class="flex items-center gap-3 px-4 py-3 rounded-xl text-sm font-semibold text-muted-foreground hover:bg-accent/50 hover:text-foreground transition-all duration-200"
          >
            <svg lucideBell class="h-5 w-5"></svg>
            Geofencing
          </a>
          <a 
            routerLink="/sos" 
            routerLinkActive="bg-accent text-accent-foreground shadow-sm"
            (click)="toggleMobileMenu()"
            class="flex items-center gap-3 px-4 py-3 rounded-xl text-sm font-semibold text-muted-foreground hover:bg-accent/50 hover:text-foreground transition-all duration-200"
          >
            <svg lucidePhone class="h-5 w-5"></svg>
            Emergency Contacts
          </a>
        </nav>
      </aside>

      <!-- MAIN APP CONTAINER -->
      <div class="flex flex-col flex-1 h-full overflow-hidden relative">
        <!-- SOS Active Banner -->
        <div 
          *ngIf="sosService.sosActive()" 
          class="bg-destructive text-destructive-foreground px-4 py-3 text-center flex items-center justify-center gap-3 font-semibold text-sm animate-pulse z-30"
        >
          <svg lucideAlertTriangle class="h-5 w-5 stroke-[2.5]"></svg>
          <span>EMERGENCY SOS ACTIVE! Location is broadcasting to emergency contacts.</span>
          <button 
            hlmBtn 
            variant="outline" 
            size="sm" 
            class="bg-card text-destructive hover:bg-destructive hover:text-destructive-foreground border-destructive/20 h-7 px-3 text-xs"
            (click)="cancelSOS()"
          >
            Cancel SOS
          </button>
        </div>

        <!-- Geofence Breach Banner -->
        <div 
          *ngIf="geofenceService.hasBreachedFences() && !sosService.sosActive()" 
          class="bg-orange-500 text-white px-4 py-3 text-center flex items-center justify-center gap-3 font-semibold text-sm z-30 shadow-md"
        >
          <svg lucideAlertTriangle class="h-5 w-5 animate-bounce"></svg>
          <span>GEOFENCE BREACH ALERT: One or more tracked devices have exited safe boundaries!</span>
          <a 
            routerLink="/tracking" 
            class="underline text-xs hover:text-orange-200 transition-colors"
          >
            Locate Device
          </a>
        </div>

        <!-- HEADER -->
        <header class="flex items-center justify-between h-16 border-b border-border bg-card px-6 z-20 shadow-sm shrink-0">
          <div class="flex items-center gap-3">
            <button 
              (click)="toggleMobileMenu()" 
              class="p-2 hover:bg-secondary rounded-lg md:hidden flex items-center justify-center"
            >
              <svg lucideShield class="h-5 w-5 text-primary"></svg>
            </button>
            <h2 class="font-bold text-lg md:text-xl tracking-tight text-foreground">SafeGuard Panel</h2>
          </div>

          <div class="flex items-center gap-4">
            <!-- Theme Toggle -->
            <button 
              (click)="toggleTheme()" 
              class="p-2.5 hover:bg-secondary rounded-xl text-muted-foreground hover:text-foreground transition-all duration-200"
              title="Toggle Theme"
            >
              <svg *ngIf="isDarkMode()" lucideSun class="h-5 w-5"></svg>
              <svg *ngIf="!isDarkMode()" lucideMoon class="h-5 w-5"></svg>
            </button>

            <!-- Quick SOS Panic Button -->
            <button 
              *ngIf="!sosService.sosActive()"
              hlmBtn 
              variant="destructive" 
              class="relative flex items-center gap-2 px-5 py-2.5 rounded-xl font-bold animate-shimmer"
              (click)="sosConfirmDialog.open()"
            >
              <span class="absolute inset-0 rounded-xl bg-destructive/30 animate-ping"></span>
              <svg lucideAlertTriangle class="h-4 w-4 shrink-0"></svg>
              <span>PANIC SOS</span>
            </button>
          </div>
        </header>

        <!-- ROUTED VIEW AREA -->
        <main class="flex-1 overflow-auto bg-background p-4 md:p-8 min-h-0">
          <router-outlet></router-outlet>
        </main>
      </div>
    </div>

    <!-- SOS CONFIRMATION DIALOG -->
    <hlm-dialog #sosConfirmDialog>
      <div class="flex flex-col gap-4 text-center">
        <div class="mx-auto p-4 bg-destructive/10 text-destructive rounded-full w-fit">
          <svg lucideAlertTriangle class="h-10 w-10 stroke-[2]"></svg>
        </div>
        <div>
          <h3 class="text-lg font-bold">Trigger Emergency SOS?</h3>
          <p class="text-sm text-muted-foreground mt-1">
            This will activate emergency protocols, fetch your exact current location, and send notification updates to your emergency contacts.
          </p>
        </div>
        <div class="flex gap-3 justify-center mt-2">
          <button hlmBtn variant="outline" (click)="sosConfirmDialog.close()">Cancel</button>
          <button hlmBtn variant="destructive" (click)="triggerSOS(sosConfirmDialog)">Trigger SOS</button>
        </div>
      </div>
    </hlm-dialog>
  `,
  styles: [
    `
      .animate-shimmer {
        background-size: 200% auto;
        animation: shine 2s linear infinite;
      }
      @keyframes shine {
        to {
          background-position: 200% center;
        }
      }
    `
  ]
})
export class AppShellComponent {
  public sosService = inject(SOSService);
  public geofenceService = inject(GeofencingService);

  public isMobileMenuOpen = signal<boolean>(false);
  public isDarkMode = signal<boolean>(false);

  constructor() {
    if (typeof window !== 'undefined') {
      const savedTheme = localStorage.getItem('theme');
      const prefersDark = window.matchMedia('(prefers-color-scheme: dark)').matches;
      if (savedTheme === 'dark' || (!savedTheme && prefersDark)) {
        this.setDarkTheme(true);
      }
    }
  }

  toggleMobileMenu() {
    this.isMobileMenuOpen.update(val => !val);
  }

  toggleTheme() {
    this.setDarkTheme(!this.isDarkMode());
  }

  private setDarkTheme(dark: boolean) {
    this.isDarkMode.set(dark);
    if (typeof window !== 'undefined') {
      const root = document.documentElement;
      if (dark) {
        root.classList.add('dark');
        localStorage.setItem('theme', 'dark');
      } else {
        root.classList.remove('dark');
        localStorage.setItem('theme', 'light');
      }
    }
  }

  triggerSOS(dialog: HlmDialogComponent) {
    this.sosService.triggerSOS();
    dialog.close();
  }

  cancelSOS() {
    this.sosService.cancelSOS();
  }
}
```

---

## 🖥 6. Feature Pages

### 6.1 Dashboard Overview (`src/app/features/dashboard/dashboard.component.ts`)
```typescript
import { Component, inject, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { LucideMapPin, LucideShieldCheck, LucideAlertTriangle, LucideShield } from '@lucide/angular';
import { TrackingService } from '../../core/services/tracking.service';
import { GeofencingService } from '../../core/services/geofencing.service';
import { SOSService } from '../../core/services/sos.service';
import { HlmCardImports } from '../../shared/ui/card';
import { HlmButtonDirective } from '../../shared/ui/button';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    CommonModule,
    LucideMapPin,
    LucideShieldCheck,
    LucideAlertTriangle,
    LucideShield,
    HlmCardImports,
    HlmButtonDirective
  ],
  template: `
    <div class="flex flex-col gap-6 md:gap-8">
      <!-- Welcome Header -->
      <div class="flex flex-col gap-1">
        <h2 class="text-2xl md:text-3xl font-bold tracking-tight text-foreground">Overview</h2>
        <p class="text-sm md:text-base text-muted-foreground">
          Real-time security and tracking statistics for all active devices.
        </p>
      </div>

      <!-- Statistics Grid -->
      <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4 md:gap-6">
        <!-- Devices Card -->
        <div hlmCard class="hover:border-primary/20 transition-all duration-300">
          <div hlmCardHeader class="flex flex-row items-center justify-between pb-2">
            <span hlmCardTitle class="text-sm font-semibold text-muted-foreground uppercase tracking-wider">Tracked Devices</span>
            <div class="p-2 bg-primary/10 rounded-xl text-primary flex items-center justify-center">
              <svg lucideMapPin class="h-5 w-5"></svg>
            </div>
          </div>
          <div hlmCardContent>
            <div class="text-3xl font-extrabold tracking-tight">{{ trackingService.devices().length }}</div>
            <p class="text-xs text-muted-foreground mt-1 flex items-center gap-1.5 font-medium">
              <span class="h-2 w-2 rounded-full bg-green-500"></span>
              {{ onlineCount() }} online and reporting
            </p>
          </div>
        </div>

        <!-- Geofences Card -->
        <div hlmCard class="hover:border-primary/20 transition-all duration-300">
          <div hlmCardHeader class="flex flex-row items-center justify-between pb-2">
            <span hlmCardTitle class="text-sm font-semibold text-muted-foreground uppercase tracking-wider">Active Geofences</span>
            <div class="p-2 bg-primary/10 rounded-xl text-primary flex items-center justify-center">
              <svg lucideShieldCheck class="h-5 w-5"></svg>
            </div>
          </div>
          <div hlmCardContent>
            <div class="text-3xl font-extrabold tracking-tight">{{ activeFencesCount() }}</div>
            <p class="text-xs text-muted-foreground mt-1 font-medium">
              Out of {{ geofencingService.geofences().length }} configured boundary zones
            </p>
          </div>
        </div>

        <!-- Active Alerts Card -->
        <div 
          hlmCard 
          class="transition-all duration-300"
          [class.border-orange-500/30]="geofencingService.hasBreachedFences()"
          [class.bg-orange-500/5]="geofencingService.hasBreachedFences()"
        >
          <div hlmCardHeader class="flex flex-row items-center justify-between pb-2">
            <span hlmCardTitle class="text-sm font-semibold text-muted-foreground uppercase tracking-wider">Active Breaches</span>
            <div 
              class="p-2 rounded-xl flex items-center justify-center"
              [class.bg-orange-500/10]="geofencingService.hasBreachedFences()"
              [class.text-orange-500]="geofencingService.hasBreachedFences()"
              [class.bg-muted]="!geofencingService.hasBreachedFences()"
              [class.text-muted-foreground]="!geofencingService.hasBreachedFences()"
            >
              <svg lucideAlertTriangle class="h-5 w-5"></svg>
            </div>
          </div>
          <div hlmCardContent>
            <div 
              class="text-3xl font-extrabold tracking-tight"
              [class.text-orange-500]="geofencingService.hasBreachedFences()"
            >
              {{ breachedFencesCount() }}
            </div>
            <p class="text-xs text-muted-foreground mt-1 font-medium">
              {{ breachedFencesCount() > 0 ? 'Urgent attention required' : 'All devices inside safe zones' }}
            </p>
          </div>
        </div>

        <!-- SOS Trigger Card -->
        <div 
          hlmCard 
          class="transition-all duration-300"
          [class.border-destructive/30]="sosService.sosActive()"
          [class.bg-destructive/5]="sosService.sosActive()"
        >
          <div hlmCardHeader class="flex flex-row items-center justify-between pb-2">
            <span hlmCardTitle class="text-sm font-semibold text-muted-foreground uppercase tracking-wider">Panic SOS Mode</span>
            <div 
              class="p-2 rounded-xl flex items-center justify-center"
              [class.bg-destructive/10]="sosService.sosActive()"
              [class.text-destructive]="sosService.sosActive()"
              [class.bg-muted]="!sosService.sosActive()"
              [class.text-muted-foreground]="!sosService.sosActive()"
            >
              <svg lucideShield class="h-5 w-5"></svg>
            </div>
          </div>
          <div hlmCardContent>
            <div 
              class="text-xl font-extrabold tracking-tight uppercase"
              [class.text-destructive]="sosService.sosActive()"
              [class.text-green-500]="!sosService.sosActive()"
            >
              {{ sosService.sosActive() ? 'ACTIVE TRIGGER' : 'STANDBY' }}
            </div>
            <p class="text-xs text-muted-foreground mt-1 font-medium">
              {{ sosService.sosActive() ? 'Emergency signal broadcasting' : 'Emergency buttons primed' }}
            </p>
          </div>
        </div>
      </div>

      <!-- Main Columns -->
      <div class="grid grid-cols-1 lg:grid-cols-3 gap-6 md:gap-8">
        <!-- Device List Panel -->
        <div hlmCard class="lg:col-span-2">
          <div hlmCardHeader class="pb-4">
            <h3 hlmCardTitle class="text-lg font-bold">Device Status Summary</h3>
            <p hlmCardDescription>Overview of monitored active battery levels and speed velocities.</p>
          </div>
          <div hlmCardContent class="flex flex-col gap-4">
            <div 
              *ngFor="let device of trackingService.devices()" 
              class="flex flex-col sm:flex-row sm:items-center justify-between p-4 bg-muted/40 border border-border/50 rounded-xl gap-4 hover:border-primary/10 transition-all duration-200"
            >
              <div class="flex items-center gap-3">
                <img [src]="device.avatar" alt="Avatar" class="h-10 w-10 rounded-full border border-border object-cover" />
                <div>
                  <h4 class="font-bold text-sm text-foreground flex items-center gap-2">
                    {{ device.name }}
                    <span 
                      class="px-2 py-0.5 rounded-full text-[10px] font-semibold"
                      [class.bg-green-500/10]="device.status === 'online'"
                      [class.text-green-500]="device.status === 'online'"
                      [class.bg-red-500/10]="device.status === 'battery-low'"
                      [class.text-red-500]="device.status === 'battery-low'"
                    >
                      {{ device.status }}
                    </span>
                  </h4>
                  <p class="text-xs text-muted-foreground font-medium mt-0.5">
                    ID: {{ device.id }} • {{ getDeviceTypeLabel(device.type) }}
                  </p>
                </div>
              </div>

              <!-- Indicators -->
              <div class="flex flex-wrap items-center gap-4 sm:gap-6 text-xs font-semibold">
                <div class="flex items-center gap-1.5">
                  <div class="flex items-center gap-1">
                    <span 
                      class="h-3 w-5 border border-foreground/30 rounded-sm relative p-0.5 flex items-center"
                      [class.border-red-500]="device.battery < 20"
                    >
                      <span 
                        class="h-full bg-foreground rounded-[1px]" 
                        [style.width.%]="device.battery"
                        [class.bg-green-500]="device.battery >= 50"
                        [class.bg-orange-500]="device.battery >= 20 && device.battery < 50"
                        [class.bg-red-500]="device.battery < 20"
                      ></span>
                    </span>
                    <span 
                      class="font-bold"
                      [class.text-red-500]="device.battery < 20"
                    >
                      {{ device.battery }}%
                    </span>
                  </div>
                </div>

                <div class="text-xs">
                  <span class="text-muted-foreground block text-[9px] uppercase font-bold tracking-wider leading-none">Speed</span>
                  <span class="text-foreground font-bold mt-0.5 block leading-none">{{ device.speed }} km/h</span>
                </div>

                <!-- Locate Action -->
                <button 
                  hlmBtn 
                  variant="outline" 
                  size="sm" 
                  class="rounded-xl h-9 px-4 font-semibold text-xs border border-border"
                  (click)="trackDevice(device.id)"
                >
                  Track Device
                </button>
              </div>
            </div>
          </div>
        </div>

        <!-- Alert Activity Feed -->
        <div hlmCard class="flex flex-col h-[400px] lg:h-auto">
          <div hlmCardHeader class="pb-3 border-b border-border/50 shrink-0">
            <h3 hlmCardTitle class="text-lg font-bold">Activity Logs</h3>
            <p hlmCardDescription>Live logs from GPS safety triggers.</p>
          </div>
          <div hlmCardContent class="overflow-y-auto pt-4 flex-1 flex flex-col gap-3.5 pr-1">
            <div 
              *ngFor="let log of geofencingService.logs()" 
              class="flex gap-3 text-xs leading-normal items-start p-3 border border-border/30 rounded-xl bg-card hover:bg-muted/30 transition-colors"
            >
              <div 
                class="p-2 rounded-xl flex items-center justify-center shrink-0 mt-0.5"
                [class.bg-green-500/10]="log.type === 'entry'"
                [class.text-green-500]="log.type === 'entry'"
                [class.bg-red-500/10]="log.type === 'exit'"
                [class.text-red-500]="log.type === 'exit'"
              >
                <svg *ngIf="log.type === 'entry'" lucideShieldCheck class="h-4 w-4"></svg>
                <svg *ngIf="log.type !== 'entry'" lucideAlertTriangle class="h-4 w-4"></svg>
              </div>
              <div class="flex-1">
                <p class="font-bold text-foreground">{{ log.message }}</p>
                <span class="text-[10px] text-muted-foreground font-semibold mt-1 block">
                  {{ log.timestamp | date:'shortTime' }} • {{ log.deviceName }}
                </span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  `
})
export class DashboardComponent {
  public trackingService = inject(TrackingService);
  public geofencingService = inject(GeofencingService);
  public sosService = inject(SOSService);
  private router = inject(Router);

  onlineCount = computed(() => {
    return this.trackingService.devices().filter(d => d.status === 'online').length;
  });

  activeFencesCount = computed(() => {
    return this.geofencingService.geofences().filter(f => f.isActive).length;
  });

  breachedFencesCount = computed(() => {
    return this.geofencingService.geofences().filter(f => f.isActive && f.status === 'breached').length;
  });

  getDeviceTypeLabel(type: string): string {
    switch (type) {
      case 'watch': return 'Smart Wearable';
      case 'collar': return 'Pet Tracker';
      case 'tracker': return 'Vehicle GPS';
      default: return 'Mobile Device';
    }
  }

  trackDevice(id: string) {
    this.trackingService.selectDevice(id);
    this.router.navigate(['/tracking']);
  }
}
```

### 6.2 Live Map Tracking (`src/app/features/tracking/tracking.component.ts`)
```typescript
import { Component, inject, computed, PLATFORM_ID, ElementRef, ViewChild, AfterViewInit, OnDestroy, effect } from '@angular/core';
import { isPlatformBrowser, CommonModule } from '@angular/common';
import { TrackingService } from '../../core/services/tracking.service';
import { HlmCardImports } from '../../shared/ui/card';
import { HlmButtonDirective } from '../../shared/ui/button';

@Component({
  selector: 'app-tracking',
  standalone: true,
  imports: [
    CommonModule,
    HlmCardImports,
    HlmButtonDirective
  ],
  template: `
    <div class="flex flex-col gap-6 h-full relative">
      <div class="flex flex-col gap-1">
        <h2 class="text-2xl md:text-3xl font-bold tracking-tight text-foreground font-sans">Live Map</h2>
        <p class="text-sm md:text-base text-muted-foreground">
          Real-time location, paths, and statuses of all devices.
        </p>
      </div>

      <!-- Map Viewport -->
      <div class="relative flex-1 w-full min-h-[500px] rounded-2xl overflow-hidden border border-border shadow-lg">
        <!-- Leaflet Container -->
        <div #mapContainer class="w-full h-full z-10"></div>

        <!-- Floating Device List Overlay -->
        <div 
          hlmCard 
          class="absolute top-4 right-4 z-20 w-80 max-h-[calc(100%-2rem)] overflow-y-auto flex flex-col p-4 shadow-xl border-border/80 bg-card/95 backdrop-blur-md"
        >
          <div hlmCardHeader class="pb-3 px-0 pt-0">
            <h3 hlmCardTitle class="text-base font-bold flex items-center justify-between">
              <span>Device Overlay</span>
              <span class="text-xs text-muted-foreground font-semibold px-2 py-0.5 bg-secondary rounded-full">
                {{ trackingService.devices().length }} Total
              </span>
            </h3>
            <p hlmCardDescription class="text-xs">Select a device to locate on map.</p>
          </div>

          <div hlmCardContent class="flex flex-col gap-2.5 px-0 pb-0 overflow-y-auto">
            <div 
              *ngFor="let device of trackingService.devices()" 
              (click)="selectDevice(device.id)"
              class="flex items-center gap-3 p-3 rounded-xl border border-border/40 bg-card hover:bg-accent/45 hover:border-primary/20 transition-all duration-200 cursor-pointer select-none"
              [class.border-primary/30]="trackingService.selectedDeviceId() === device.id"
              [class.bg-accent/40]="trackingService.selectedDeviceId() === device.id"
            >
              <img [src]="device.avatar" alt="Avatar" class="h-9 w-9 rounded-full object-cover border border-border shadow-sm" />
              <div class="flex-1 min-w-0">
                <h4 class="font-bold text-xs flex items-center justify-between text-foreground">
                  <span class="truncate pr-1">{{ device.name }}</span>
                  <span 
                    class="h-2 w-2 rounded-full shrink-0" 
                    [class.bg-green-500]="device.status === 'online'"
                    [class.bg-red-500]="device.status === 'battery-low'"
                    [class.bg-muted-foreground]="device.status === 'offline'"
                  ></span>
                </h4>
                <div class="flex items-center justify-between text-[10px] text-muted-foreground font-medium mt-1">
                  <span>{{ device.speed }} km/h</span>
                  <span [class.text-red-500]="device.battery < 20">{{ device.battery }}% Batt</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  `
})
export class TrackingComponent implements AfterViewInit, OnDestroy {
  public trackingService = inject(TrackingService);
  private platformId = inject(PLATFORM_ID);

  @ViewChild('mapContainer', { static: false }) mapContainer!: ElementRef;

  private map: any;
  private markersMap = new Map<string, any>();
  private pathsMap = new Map<string, any>();
  private L: any = null;

  constructor() {
    effect(async () => {
      const devices = this.trackingService.devices();
      const selectedDevice = this.trackingService.selectedDevice();
      const history = this.trackingService.history();

      if (!this.map || !this.L) return;

      devices.forEach(device => {
        const marker = this.markersMap.get(device.id);
        const position = [device.latitude, device.longitude] as [number, number];

        if (marker) {
          marker.setLatLng(position);

          const popupContent = `
            <div class="p-1 font-sans text-xs">
              <h5 class="font-bold text-sm text-foreground flex items-center gap-1.5">${device.name}</h5>
              <p class="text-muted-foreground mt-1 font-medium">Battery: <span class="font-bold text-foreground">${device.battery}%</span></p>
              <p class="text-muted-foreground font-medium">Speed: <span class="font-bold text-foreground">${device.speed} km/h</span></p>
            </div>
          `;
          marker.getPopup().setContent(popupContent);
        } else {
          const iconHtml = `
            <div class="relative flex items-center justify-center h-10 w-10">
              <div class="absolute h-9 w-9 bg-primary/20 rounded-full animate-ping"></div>
              <div class="h-8 w-8 rounded-full border-2 border-primary bg-card overflow-hidden shadow-md relative">
                <img src="${device.avatar}" class="h-full w-full object-cover" />
              </div>
            </div>
          `;

          const customIcon = this.L.divIcon({
            html: iconHtml,
            className: 'custom-device-marker',
            iconSize: [40, 40],
            iconAnchor: [20, 20]
          });

          const newMarker = this.L.marker(position, { icon: customIcon }).addTo(this.map);
          newMarker.bindPopup(`
            <div class="p-1 font-sans text-xs">
              <h5 class="font-bold text-sm text-foreground">${device.name}</h5>
              <p class="text-muted-foreground mt-1">Battery: <span class="font-bold text-foreground">${device.battery}%</span></p>
              <p class="text-muted-foreground">Speed: <span class="font-bold text-foreground">${device.speed} km/h</span></p>
            </div>
          `);

          this.markersMap.set(device.id, newMarker);
        }

        const deviceHistory = history[device.id] || [];
        if (deviceHistory.length > 0) {
          const polyline = this.pathsMap.get(device.id);
          if (polyline) {
            polyline.setLatLngs(deviceHistory);
          } else {
            const newPolyline = this.L.polyline(deviceHistory, {
              color: 'var(--primary)',
              weight: 3,
              opacity: 0.6,
              dashArray: '5, 8'
            }).addTo(this.map);
            this.pathsMap.set(device.id, newPolyline);
          }
        }
      });

      if (selectedDevice) {
        this.map.panTo([selectedDevice.latitude, selectedDevice.longitude]);
      }
    });
  }

  async ngAfterViewInit() {
    if (isPlatformBrowser(this.platformId)) {
      this.L = await import('leaflet');

      this.map = this.L.map(this.mapContainer.nativeElement, {
        zoomControl: true,
        attributionControl: false
      }).setView([37.7749, -122.4194], 14.5);

      this.L.tileLayer('https://{s}.basemaps.cartocdn.com/rastertiles/voyager/{z}/{x}/{y}{r}.png', {
        maxZoom: 20
      }).addTo(this.map);

      setTimeout(() => {
        this.map.invalidateSize();
      }, 200);
    }
  }

  selectDevice(id: string) {
    this.trackingService.selectDevice(id);
    const device = this.trackingService.devices().find(d => d.id === id);
    if (device && this.map) {
      this.map.setView([device.latitude, device.longitude], 15.5);
      const marker = this.markersMap.get(id);
      if (marker) {
        marker.openPopup();
      }
    }
  }

  ngOnDestroy() {
    if (this.map) {
      this.map.remove();
    }
  }
}
```

### 6.3 Geofencing Editor (`src/app/features/geofencing/geofencing.component.ts`)
```typescript
import { Component, inject, computed, signal, PLATFORM_ID, ElementRef, ViewChild, AfterViewInit, OnDestroy, effect } from '@angular/core';
import { isPlatformBrowser, CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { LucideInfo, LucideX, LucideTrash, LucidePlay, LucideSquare } from '@lucide/angular';
import { GeofencingService } from '../../core/services/geofencing.service';
import { Geofence } from '../../core/models/geofence.model';
import { HlmCardImports } from '../../shared/ui/card';
import { HlmButtonDirective } from '../../shared/ui/button';
import { HlmSliderComponent } from '../../shared/ui/slider';

@Component({
  selector: 'app-geofencing',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    LucideInfo,
    LucideX,
    LucideTrash,
    LucidePlay,
    LucideSquare,
    HlmCardImports,
    HlmButtonDirective,
    HlmSliderComponent
  ],
  template: `
    <div class="flex flex-col gap-6 md:gap-8 h-full">
      <div class="flex flex-col gap-1">
        <h2 class="text-2xl md:text-3xl font-bold tracking-tight text-foreground font-sans">Geofence Boundaries</h2>
        <p class="text-sm md:text-base text-muted-foreground">
          Define virtual safe zones on the map and configure alert triggers.
        </p>
      </div>

      <!-- Main Columns -->
      <div class="grid grid-cols-1 lg:grid-cols-3 gap-6 md:gap-8 flex-1 min-h-[500px]">
        <!-- Map Panel -->
        <div class="lg:col-span-2 relative rounded-2xl overflow-hidden border border-border shadow-lg bg-card flex flex-col">
          <!-- Instruction bar -->
          <div class="bg-primary/5 text-primary text-xs font-semibold px-4 py-2 border-b border-border/50 flex items-center gap-2">
            <svg lucideInfo class="h-4 w-4"></svg>
            <span>Click anywhere on the map to define the center of a new boundary zone.</span>
          </div>
          <div #mapContainer class="flex-1 w-full h-full z-10"></div>
        </div>

        <!-- Configuration Sidebar -->
        <div class="flex flex-col gap-6">
          <!-- Setup Boundary Form -->
          <div hlmCard *ngIf="showSetupPanel()" class="border-primary/30 bg-primary/[0.02]">
            <div hlmCardHeader class="pb-3">
              <h3 hlmCardTitle class="text-base font-bold flex items-center justify-between">
                <span>New Boundary</span>
                <button (click)="cancelSetup()" class="p-1 hover:bg-secondary rounded-lg">
                  <svg lucideX class="h-4 w-4"></svg>
                </button>
              </h3>
              <p hlmCardDescription class="text-xs">Customize the coordinates and size of this zone.</p>
            </div>
            <div hlmCardContent class="flex flex-col gap-4">
              <!-- Name Input -->
              <div class="flex flex-col gap-1.5">
                <label class="text-xs font-semibold text-muted-foreground uppercase tracking-wider">Zone Name</label>
                <input 
                  type="text" 
                  [(ngModel)]="newFenceName" 
                  placeholder="e.g. Grandma's House" 
                  class="h-10 px-3 bg-background border border-border rounded-lg text-sm text-foreground focus:outline-none focus:ring-2 focus:ring-primary/20"
                />
              </div>

              <!-- Coordinate Display -->
              <div class="grid grid-cols-2 gap-3 text-xs bg-muted/40 p-3 rounded-lg border border-border/30">
                <div>
                  <span class="text-muted-foreground block text-[9px] uppercase tracking-wider leading-none">Latitude</span>
                  <span class="text-foreground font-bold mt-1 block leading-none">{{ tempCenter()?.[0] | number:'1.5-5' }}</span>
                </div>
                <div>
                  <span class="text-muted-foreground block text-[9px] uppercase tracking-wider leading-none">Longitude</span>
                  <span class="text-foreground font-bold mt-1 block leading-none">{{ tempCenter()?.[1] | number:'1.5-5' }}</span>
                </div>
              </div>

              <!-- Radius Slider -->
              <hlm-slider 
                label="Safety Radius"
                [min]="50"
                [max]="1000"
                [step]="25"
                [value]="newFenceRadius()"
                unit=" meters"
                (valueChange)="updateRadius($event)"
              ></hlm-slider>

              <button 
                hlmBtn 
                variant="default" 
                class="w-full mt-2 font-bold"
                [disabled]="!newFenceName.trim()"
                (click)="saveGeofence()"
              >
                Save Safety Zone
              </button>
            </div>
          </div>

          <!-- Active Boundaries List -->
          <div hlmCard class="flex-1 overflow-y-auto">
            <div hlmCardHeader class="pb-3 border-b border-border/50">
              <h3 hlmCardTitle class="text-base font-bold">Boundary Zones</h3>
              <p hlmCardDescription class="text-xs">Manage active safety boundaries.</p>
            </div>
            <div hlmCardContent class="pt-4 flex flex-col gap-3 overflow-y-auto pr-1">
              <div *ngIf="geofencingService.geofences().length === 0" class="text-center py-6 text-xs text-muted-foreground font-medium">
                No configured zones. Click the map to create one.
              </div>
              <div 
                *ngFor="let fence of geofencingService.geofences()" 
                class="flex items-center justify-between p-3.5 border border-border/50 rounded-xl bg-muted/20 hover:border-primary/10 transition-colors"
              >
                <div class="flex-1 min-w-0">
                  <h4 class="font-bold text-xs text-foreground flex items-center gap-1.5">
                    {{ fence.name }}
                    <span 
                      class="h-2 w-2 rounded-full"
                      [class.bg-green-500]="fence.isActive && fence.status === 'safe'"
                      [class.bg-red-500]="fence.isActive && fence.status === 'breached'"
                      [class.bg-muted-foreground]="!fence.isActive"
                    ></span>
                  </h4>
                  <p class="text-[10px] text-muted-foreground font-semibold mt-1">
                    Radius: {{ fence.radius }}m • Status: 
                    <span class="font-bold" [class.text-red-500]="fence.status === 'breached'" [class.text-green-500]="fence.status === 'safe'">
                      {{ fence.isActive ? (fence.status === 'breached' ? 'BREACHED' : 'SAFE') : 'INACTIVE' }}
                    </span>
                  </p>
                </div>

                <div class="flex items-center gap-2">
                  <!-- Toggle status -->
                  <button 
                    (click)="geofencingService.toggleGeofence(fence.id)"
                    class="p-1.5 hover:bg-secondary rounded-lg text-muted-foreground hover:text-foreground transition-colors"
                    title="Toggle active status"
                  >
                    <svg *ngIf="fence.isActive" lucidePlay class="h-4 w-4"></svg>
                    <svg *ngIf="!fence.isActive" lucideSquare class="h-4 w-4"></svg>
                  </button>
                  <!-- Delete -->
                  <button 
                    (click)="geofencingService.deleteGeofence(fence.id)"
                    class="p-1.5 hover:bg-destructive/10 text-muted-foreground hover:text-destructive rounded-lg transition-colors"
                    title="Delete zone"
                  >
                    <svg lucideTrash class="h-4 w-4"></svg>
                  </button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  `
})
export class GeofencingComponent implements AfterViewInit, OnDestroy {
  public geofencingService = inject(GeofencingService);
  private platformId = inject(PLATFORM_ID);

  @ViewChild('mapContainer', { static: false }) mapContainer!: ElementRef;

  private map: any;
  private L: any = null;

  public showSetupPanel = signal<boolean>(false);
  public tempCenter = signal<[number, number] | null>(null);
  public newFenceRadius = signal<number>(200);
  public newFenceName: string = '';

  private tempMarker: any;
  private tempCircle: any;

  private activeFencesMap = new Map<string, any>(); // id -> leaflet circle

  constructor() {
    effect(() => {
      const fences = this.geofencingService.geofences();
      if (!this.map || !this.L) return;

      this.activeFencesMap.forEach((circle, id) => {
        if (!fences.some(f => f.id === id)) {
          circle.remove();
          this.activeFencesMap.delete(id);
        }
      });

      fences.forEach(fence => {
        const existingCircle = this.activeFencesMap.get(fence.id);
        const position = [fence.latitude, fence.longitude] as [number, number];
        const statusColor = fence.isActive 
          ? (fence.status === 'breached' ? '#ef4444' : '#10b981')
          : '#9ca3af';

        if (existingCircle) {
          existingCircle.setLatLng(position);
          existingCircle.setRadius(fence.radius);
          existingCircle.setStyle({
            color: statusColor,
            fillColor: statusColor
          });
        } else {
          const circle = this.L.circle(position, {
            radius: fence.radius,
            color: statusColor,
            fillColor: statusColor,
            fillOpacity: 0.12,
            weight: 2
          }).addTo(this.map);

          circle.bindPopup(`<strong class="text-xs font-sans text-foreground">${fence.name}</strong>`);
          this.activeFencesMap.set(fence.id, circle);
        }
      });
    });
  }

  async ngAfterViewInit() {
    if (isPlatformBrowser(this.platformId)) {
      this.L = await import('leaflet');

      this.map = this.L.map(this.mapContainer.nativeElement, {
        zoomControl: true,
        attributionControl: false
      }).setView([37.7749, -122.4194], 14.5);

      this.L.tileLayer('https://{s}.basemaps.cartocdn.com/rastertiles/voyager/{z}/{x}/{y}{r}.png', {
        maxZoom: 20
      }).addTo(this.map);

      this.map.on('click', (e: any) => {
        this.setupNewBoundary([e.latlng.lat, e.latlng.lng]);
      });

      setTimeout(() => {
        this.map.invalidateSize();
      }, 200);
    }
  }

  setupNewBoundary(coords: [number, number]) {
    this.tempCenter.set(coords);
    this.showSetupPanel.set(true);

    if (!this.map || !this.L) return;

    if (this.tempMarker) {
      this.tempMarker.setLatLng(coords);
      this.tempCircle.setLatLng(coords);
    } else {
      this.tempMarker = this.L.marker(coords, {
        draggable: true
      }).addTo(this.map);

      this.tempCircle = this.L.circle(coords, {
        radius: this.newFenceRadius(),
        color: 'var(--primary)',
        fillColor: 'var(--primary)',
        fillOpacity: 0.15,
        weight: 2,
        dashArray: '5, 5'
      }).addTo(this.map);

      this.tempMarker.on('drag', (e: any) => {
        const dragCoords = [e.target.getLatLng().lat, e.target.getLatLng().lng] as [number, number];
        this.tempCenter.set(dragCoords);
        this.tempCircle.setLatLng(dragCoords);
      });
    }

    this.map.panTo(coords);
  }

  updateRadius(val: number) {
    this.newFenceRadius.set(val);
    if (this.tempCircle) {
      this.tempCircle.setRadius(val);
    }
  }

  saveGeofence() {
    const center = this.tempCenter();
    if (!center || !this.newFenceName.trim()) return;

    const newFence: Geofence = {
      id: 'fence-' + Math.random().toString(36).substring(2, 9),
      name: this.newFenceName,
      latitude: center[0],
      longitude: center[1],
      radius: this.newFenceRadius(),
      isActive: true,
      status: 'safe'
    };

    this.geofencingService.addGeofence(newFence);
    this.cancelSetup();
  }

  cancelSetup() {
    this.showSetupPanel.set(false);
    this.tempCenter.set(null);
    this.newFenceName = '';
    this.newFenceRadius.set(200);

    if (this.tempMarker) {
      this.tempMarker.remove();
      this.tempMarker = null;
    }
    if (this.tempCircle) {
      this.tempCircle.remove();
      this.tempCircle = null;
    }
  }

  ngOnDestroy() {
    if (this.map) {
      this.map.remove();
    }
  }
}
```

### 6.4 SOS Contacts & Emergency (`src/app/features/sos/sos.component.ts`)
```typescript
import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { LucideAlertTriangle, LucideNavigation, LucidePlus, LucideTrash } from '@lucide/angular';
import { SOSService } from '../../core/services/sos.service';
import { SOSContact } from '../../core/models/sos-contact.model';
import { HlmCardImports } from '../../shared/ui/card';
import { HlmButtonDirective } from '../../shared/ui/button';
import { HlmDialogComponent } from '../../shared/ui/dialog';

@Component({
  selector: 'app-sos',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    LucideAlertTriangle,
    LucideNavigation,
    LucidePlus,
    LucideTrash,
    HlmCardImports,
    HlmButtonDirective,
    HlmDialogComponent
  ],
  template: `
    <div class="flex flex-col gap-6 md:gap-8 h-full">
      <!-- Title Header -->
      <div class="flex flex-col gap-1">
        <h2 class="text-2xl md:text-3xl font-bold tracking-tight text-foreground font-sans">Emergency Center</h2>
        <p class="text-sm md:text-base text-muted-foreground">
          Trigger simulated system-wide SOS alerts and manage emergency notification recipients.
        </p>
      </div>

      <!-- Main Columns -->
      <div class="grid grid-cols-1 lg:grid-cols-3 gap-6 md:gap-8 flex-1">
        <!-- SOS Trigger Card -->
        <div 
          hlmCard 
          class="lg:col-span-1 flex flex-col items-center justify-center p-8 text-center transition-all duration-300 min-h-[350px]"
          [class.border-destructive/30]="sosService.sosActive()"
          [class.bg-destructive/[0.02]]="sosService.sosActive()"
        >
          <div hlmCardHeader class="items-center pb-2">
            <h3 hlmCardTitle class="text-lg font-bold">Panic SOS Signal</h3>
            <p hlmCardDescription>Activate to simulate real-time coordinate broadcast.</p>
          </div>

          <div hlmCardContent class="flex flex-col items-center justify-center py-6 w-full flex-1 gap-6">
            <!-- Big Pulsing SOS Trigger Button -->
            <button
              (click)="handleSOSButton()"
              class="h-36 w-36 rounded-full flex flex-col items-center justify-center text-white font-extrabold border-8 border-card shadow-2xl relative select-none cursor-pointer focus:outline-none transition-transform duration-300 active:scale-95 bg-destructive"
              [style.box-shadow]="sosService.sosActive() ? '0 0 50px rgba(239, 68, 68, 0.4)' : '0 10px 25px -5px rgba(239, 68, 68, 0.3)'"
            >
              <!-- Pulsing Waves -->
              <span 
                *ngIf="sosService.sosActive()" 
                class="absolute inset-0 rounded-full bg-destructive/30 animate-ping -m-2"
              ></span>
              <span 
                *ngIf="sosService.sosActive()" 
                class="absolute inset-0 rounded-full bg-destructive/20 animate-pulse -m-4"
              ></span>

              <svg lucideAlertTriangle class="h-10 w-10 mb-1.5 stroke-[2]"></svg>
              <span class="text-xl tracking-wider">{{ sosService.sosActive() ? 'STOP' : 'SOS' }}</span>
            </button>

            <!-- Coordinate Info -->
            <div class="text-xs w-full">
              <span class="text-muted-foreground font-medium block">SOS Coordinates</span>
              <div 
                *ngIf="sosService.sosActive() && sosService.sosCoordinates(); else standbyText"
                class="font-bold text-foreground mt-1.5 bg-muted/50 p-2.5 rounded-xl border border-border/40 inline-flex items-center gap-1.5"
              >
                <svg lucideNavigation class="h-3.5 w-3.5 text-destructive animate-pulse"></svg>
                <span>
                  {{ sosService.sosCoordinates()?.[0] | number:'1.4-4' }}, 
                  {{ sosService.sosCoordinates()?.[1] | number:'1.4-4' }}
                </span>
              </div>
              <ng-template #standbyText>
                <span class="font-bold text-green-500 mt-1 block">PRIMED & STANDBY</span>
              </ng-template>
            </div>
          </div>
        </div>

        <!-- Emergency Contacts Manager -->
        <div hlmCard class="lg:col-span-2 flex flex-col">
          <div hlmCardHeader class="flex flex-row items-center justify-between pb-4 border-b border-border/50">
            <div>
              <h3 hlmCardTitle class="text-lg font-bold">Emergency Contacts</h3>
              <p hlmCardDescription class="text-xs">Notifications will be sent to this list upon SOS triggers.</p>
            </div>
            <!-- Add Contact Action -->
            <button 
              hlmBtn 
              variant="default" 
              size="sm" 
              class="rounded-xl flex items-center gap-1.5 font-bold"
              (click)="openAddContactDialog(contactDialog)"
            >
              <svg lucidePlus class="h-4 w-4"></svg>
              <span>Add Recipient</span>
            </button>
          </div>

          <div hlmCardContent class="pt-4 flex flex-col gap-3.5 flex-1 overflow-y-auto pr-1">
            <div *ngIf="sosService.contacts().length === 0" class="text-center py-10 text-xs text-muted-foreground font-medium">
              No contacts configured. Click "Add Recipient" to add.
            </div>

            <div 
              *ngFor="let contact of sosService.contacts()" 
              class="flex flex-col sm:flex-row sm:items-center justify-between p-4 border border-border/50 rounded-xl bg-muted/20 hover:border-primary/10 transition-colors gap-4"
            >
              <div class="flex items-center gap-3">
                <div class="h-10 w-10 rounded-full bg-primary/10 text-primary flex items-center justify-center font-bold text-sm shrink-0">
                  {{ contact.name.substring(0,2).toUpperCase() }}
                </div>
                <div>
                  <h4 class="font-bold text-sm text-foreground flex items-center gap-2">
                    {{ contact.name }}
                    <span class="px-2 py-0.5 rounded-full bg-secondary text-[10px] text-muted-foreground font-semibold">
                      {{ contact.relation }}
                    </span>
                  </h4>
                  <p class="text-xs text-muted-foreground font-medium mt-1">
                    {{ contact.phone }} • {{ contact.email }}
                  </p>
                </div>
              </div>

              <!-- Contact Control Actions -->
              <div class="flex items-center justify-end gap-3.5">
                <div class="flex items-center gap-2">
                  <label class="text-xs text-muted-foreground font-semibold flex items-center gap-2 cursor-pointer select-none">
                    <input 
                      type="checkbox" 
                      [checked]="contact.isAlertEnabled" 
                      (change)="toggleAlert(contact)"
                      class="h-4 w-4 rounded border-border text-primary focus:ring-primary/20 accent-primary"
                    />
                    Receive Alerts
                  </label>
                </div>
                <!-- Delete button -->
                <button 
                  (click)="sosService.deleteContact(contact.id)"
                  class="p-2 hover:bg-destructive/10 text-muted-foreground hover:text-destructive rounded-lg transition-colors"
                  title="Remove contact"
                >
                  <svg lucideTrash class="h-4 w-4"></svg>
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- ADD/EDIT CONTACT DIALOG -->
    <hlm-dialog #contactDialog>
      <h3 class="text-lg font-bold border-b border-border pb-2">Add Contact Recipient</h3>
      <form (submit)="saveContact(contactDialog)" class="flex flex-col gap-4 mt-2">
        <!-- Name -->
        <div class="flex flex-col gap-1.5">
          <label class="text-xs font-semibold text-muted-foreground uppercase tracking-wider">Contact Name</label>
          <input 
            type="text" 
            [(ngModel)]="formName" 
            name="name"
            required
            placeholder="e.g. John Connor" 
            class="h-10 px-3 bg-background border border-border rounded-lg text-sm text-foreground focus:outline-none focus:ring-2 focus:ring-primary/20"
          />
        </div>

        <!-- Relation -->
        <div class="flex flex-col gap-1.5">
          <label class="text-xs font-semibold text-muted-foreground uppercase tracking-wider">Relationship</label>
          <input 
            type="text" 
            [(ngModel)]="formRelation" 
            name="relation"
            required
            placeholder="e.g. Mother, Guardian, Doctor" 
            class="h-10 px-3 bg-background border border-border rounded-lg text-sm text-foreground focus:outline-none focus:ring-2 focus:ring-primary/20"
          />
        </div>

        <!-- Phone -->
        <div class="flex flex-col gap-1.5">
          <label class="text-xs font-semibold text-muted-foreground uppercase tracking-wider">Phone Number</label>
          <input 
            type="tel" 
            [(ngModel)]="formPhone" 
            name="phone"
            required
            placeholder="+1 (555) 0123" 
            class="h-10 px-3 bg-background border border-border rounded-lg text-sm text-foreground focus:outline-none focus:ring-2 focus:ring-primary/20"
          />
        </div>

        <!-- Email -->
        <div class="flex flex-col gap-1.5">
          <label class="text-xs font-semibold text-muted-foreground uppercase tracking-wider">Email Address</label>
          <input 
            type="email" 
            [(ngModel)]="formEmail" 
            name="email"
            required
            placeholder="recipient&#64;example.com" 
            class="h-10 px-3 bg-background border border-border rounded-lg text-sm text-foreground focus:outline-none focus:ring-2 focus:ring-primary/20"
          />
        </div>

        <!-- Buttons -->
        <div class="flex justify-end gap-3 border-t border-border pt-4 mt-2">
          <button type="button" hlmBtn variant="outline" (click)="contactDialog.close()">Cancel</button>
          <button 
            type="submit" 
            hlmBtn 
            variant="default"
            [disabled]="!formName.trim() || !formRelation.trim() || !formPhone.trim() || !formEmail.trim()"
          >
            Add Contact
          </button>
        </div>
      </form>
    </hlm-dialog>
  `
})
export class SOSComponent {
  public sosService = inject(SOSService);

  formName: string = '';
  formRelation: string = '';
  formPhone: string = '';
  formEmail: string = '';

  handleSOSButton() {
    if (this.sosService.sosActive()) {
      this.sosService.cancelSOS();
    } else {
      this.sosService.triggerSOS();
    }
  }

  toggleAlert(contact: SOSContact) {
    this.sosService.updateContact({
      ...contact,
      isAlertEnabled: !contact.isAlertEnabled
    });
  }

  openAddContactDialog(dialog: HlmDialogComponent) {
    this.formName = '';
    this.formRelation = '';
    this.formPhone = '';
    this.formEmail = '';
    dialog.open();
  }

  saveContact(dialog: HlmDialogComponent) {
    if (
      !this.formName.trim() || 
      !this.formRelation.trim() || 
      !this.formPhone.trim() || 
      !this.formEmail.trim()
    ) return;

    this.sosService.addContact({
      name: this.formName,
      relation: this.formRelation,
      phone: this.formPhone,
      email: this.formEmail,
      isAlertEnabled: true
    });

    dialog.close();
  }
}
```

---

## 🧭 7. App Routing Configuration

### 7.1 Routes definition (`src/app/app.routes.ts`)
```typescript
import { Routes } from '@angular/router';
import { DashboardComponent } from './features/dashboard/dashboard.component';
import { TrackingComponent } from './features/tracking/tracking.component';
import { GeofencingComponent } from './features/geofencing/geofencing.component';
import { SOSComponent } from './features/sos/sos.component';

export const routes: Routes = [
  { path: 'dashboard', component: DashboardComponent },
  { path: 'tracking', component: TrackingComponent },
  { path: 'geofencing', component: GeofencingComponent },
  { path: 'sos', component: SOSComponent },
  { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
  { path: '**', redirectTo: 'dashboard' }
];
```
