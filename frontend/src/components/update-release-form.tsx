'use client'

import { useState } from 'react'
import { Input } from '@/components/ui/input'
import { Button } from '@/components/ui/button'
import {
  existsByDocumentAndTypeQueryParamsTypeEnum as T,
  ExistsByDocumentAndTypeQueryParamsTypeEnum as Type,
} from '@/api/models/ExistsByDocumentAndType'

import { useUpdateDeliveryRelease } from '@/api/hooks/useUpdateDeliveryRelease'
import { useUpdateDriverRelease } from '@/api/hooks/useUpdateDriverRelease'
import { useUpdateFamilyRelease } from '@/api/hooks/useUpdateFamilyRelease'
import { useUpdateGuestRelease } from '@/api/hooks/useUpdateGuestRelease'
import { useUpdateServiceProviderRelease } from '@/api/hooks/useUpdateServiceProviderRelease'

import { useDeleteDeliveryRelease } from '@/api/hooks/useDeleteDeliveryRelease'
import { useDeleteDriverRelease } from '@/api/hooks/useDeleteDriverRelease'
import { useDeleteFamilyRelease } from '@/api/hooks/useDeleteFamilyRelease'
import { useDeleteGuestRelease } from '@/api/hooks/useDeleteGuestRelease'
import { useDeleteServiceProviderRelease } from '@/api/hooks/useDeleteServiceProviderRelease'

interface Props {
  release: any
  propertyId: string
  onSuccess: () => void
}

// Helper function to convert ISO string to local datetime-local format
function isoToLocalDateTime(isoString: string): string {
  if (!isoString) return ''
  const date = new Date(isoString)
  const year = date.getFullYear()
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')
  const hours = String(date.getHours()).padStart(2, '0')
  const minutes = String(date.getMinutes()).padStart(2, '0')
  return `${year}-${month}-${day}T${hours}:${minutes}`
}

// Helper function to convert ISO string to local time format
function isoToLocalTime(isoString: string): string {
  if (!isoString) return ''
  const date = new Date(isoString)
  const hours = String(date.getHours()).padStart(2, '0')
  const minutes = String(date.getMinutes()).padStart(2, '0')
  return `${hours}:${minutes}`
}

// Helper function to convert local datetime to ISO with timezone
function localDateTimeToIso(localDateTime: string): string {
  if (!localDateTime) return ''
  const date = new Date(localDateTime)
  return date.toISOString()
}

// Helper function to create ISO datetime from date and time
function createIsoFromDateAndTime(dateStr: string, timeStr: string): string {
  if (!dateStr || !timeStr) return ''
  const date = new Date(`${dateStr}T${timeStr}:00`)
  return date.toISOString()
}

export default function UpdateReleaseForm({ release, propertyId, onSuccess }: Props) {
  // Initialize state with properly converted values from ISO strings
  const [validFrom, setValidFrom] = useState(isoToLocalDateTime(release.validFrom))
  const [validUntil, setValidUntil] = useState(isoToLocalDateTime(release.validUntil))
  const [dailyStart, setDailyStart] = useState(isoToLocalTime(release.dailyStart))
  const [dailyEnd, setDailyEnd] = useState(isoToLocalTime(release.dailyEnd))

  const type: Type = release.releaseType

  const updateMutation = (() => {
    switch (type) {
      case T.DELIVERY: return useUpdateDeliveryRelease
      case T.DRIVER: return useUpdateDriverRelease
      case T.FAMILY: return useUpdateFamilyRelease
      case T.GUEST: return useUpdateGuestRelease
      case T.SERVICEPROVIDER: return useUpdateServiceProviderRelease
      default: throw new Error(`Tipo de liberação inválido: ${type}`)
    }
  })()({ mutation: { onSuccess } })

  const deleteMutation = (() => {
    switch (type) {
      case T.DELIVERY: return useDeleteDeliveryRelease
      case T.DRIVER: return useDeleteDriverRelease
      case T.FAMILY: return useDeleteFamilyRelease
      case T.GUEST: return useDeleteGuestRelease
      case T.SERVICEPROVIDER: return useDeleteServiceProviderRelease
      default: throw new Error(`Tipo de liberação inválido: ${type}`)
    }
  })()({ mutation: { onSuccess } })

  const isValid = () => {
    if (type === T.SERVICEPROVIDER) {
      // For service providers, ensure dailyEnd is after dailyStart
      const startTime = new Date(`2000-01-01T${dailyStart}`)
      const endTime = new Date(`2000-01-01T${dailyEnd}`)
      if (endTime <= startTime) {
        return false
      }
    }
    return validFrom && validUntil && dailyStart && dailyEnd
  }

  const handleSave = () => {
    if (!isValid()) return

    // Extract the date part from validFrom for daily times
    const datePart = validFrom.split('T')[0]
    
    const payload = {
      unitId: propertyId,
      validFrom: localDateTimeToIso(validFrom),
      validUntil: localDateTimeToIso(validUntil),
      dailyStart: createIsoFromDateAndTime(datePart, dailyStart),
      dailyEnd: createIsoFromDateAndTime(datePart, dailyEnd),
    }

    console.log('Payload being sent:', payload) // Debug log

    updateMutation.mutate({
      id: release.id,
      data: payload
    })
  }

  const handleDelete = () => {
    if (confirm('Tem certeza que deseja excluir esta liberação?')) {
      deleteMutation.mutate({ id: release.id })
    }
  }

  return (
    <div className="space-y-4">
      <h2 className="text-lg font-semibold">Editar horários da liberação ({type})</h2>

      <label className="text-sm font-medium">Início da liberação</label>
      <Input type="datetime-local" value={validFrom} onChange={(e) => setValidFrom(e.target.value)} />

      <label className="text-sm font-medium">Fim da liberação</label>
      <Input type="datetime-local" value={validUntil} onChange={(e) => setValidUntil(e.target.value)} />

      <label className="text-sm font-medium">Horário diário de entrada</label>
      <Input type="time" value={dailyStart} onChange={(e) => setDailyStart(e.target.value)} />

      <label className="text-sm font-medium">Horário diário de saída</label>
      <Input type="time" value={dailyEnd} onChange={(e) => setDailyEnd(e.target.value)} />

      {type === T.SERVICEPROVIDER && (
        <div className="mt-4 p-4 bg-yellow-50 border border-yellow-200 rounded-md">
          <h3 className="text-sm font-medium text-yellow-800">Regras para Prestadores de Serviço</h3>
          <ul className="mt-2 text-sm text-yellow-700 list-disc list-inside space-y-1">
            <li>O prestador só poderá entrar no horário permitido</li>
            <li>A saída deve ser feita dentro do horário estabelecido</li>
            <li>Em caso de necessidade de saída após o horário, será necessário justificativa e aprovação do morador</li>
          </ul>
        </div>
      )}

      <div className="space-y-2">
        <Button className="w-full mt-4" onClick={handleSave} disabled={!isValid()}>
          Salvar alterações
        </Button>

        <Button
          variant="destructive"
          className="w-full"
          onClick={handleDelete}
        >
          Excluir Liberação
        </Button>
      </div>

      {/* Debug section - remove in production */}
      {/* <div className="mt-4 p-2 bg-gray-100 text-xs rounded">
        <strong>Debug Info:</strong><br/>
        Original dailyStart: {release.dailyStart}<br/>
        Parsed dailyStart: {dailyStart}<br/>
        Will send: {validFrom ? createIsoFromDateAndTime(validFrom.split('T')[0], dailyStart) : 'N/A'}
      </div> */}
    </div>
  )
}